{
  description = "sbt built from this checkout";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-26.05";

  outputs =
    { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };
      inherit (pkgs) lib;
      jdk = pkgs.jdk21;
      version = "2.1.0-SNAPSHOT";
      buildDefinition = lib.cleanSourceWith {
        src = self;
        filter =
          path: type:
          let
            relative = lib.removePrefix "${self}/" (toString path);
          in
          relative == "build.sbt"
          || (type == "directory" && !lib.hasInfix "/" relative)
          || relative == "project"
          || relative == "launcher-package"
          || (
            (lib.hasPrefix "project/" relative || lib.hasPrefix "launcher-package/" relative)
            && (
              type == "directory"
              || lib.hasSuffix ".scala" relative
              || lib.hasSuffix ".sbt" relative
              || lib.hasSuffix ".properties" relative
            )
          );
      };
      sbtDependencies = pkgs.stdenvNoCC.mkDerivation {
        pname = "sbt-dependencies";
        inherit version;
        src = buildDefinition;
        nativeBuildInputs = [ pkgs.sbt pkgs.coursier jdk pkgs.cacert ];
        outputHashMode = "recursive";
        outputHashAlgo = "sha256";
        outputHash = "sha256-rK2jLTje98XoiZWssqviyLtPx0Un8Ecfn33m2TC4YaA=";

        buildPhase = ''
          runHook preBuild
          export HOME="$TMPDIR/home"
          mkdir -p "$HOME" "$out"
          export JAVA_HOME=${jdk}
          export CI=true
          export COURSIER_CACHE="$out/coursier"
          cs fetch ch.epfl.scala:scalafix-cli_3.9.0:0.14.9 >/dev/null
          cs fetch ch.epfl.scala:scalafix-cli_2.12.21:0.14.9 >/dev/null
          cs fetch net.hamnaberg:dataclass-scalafix_2.13:0.3.0 >/dev/null
          sbt --server -batch -Dsbt.io.virtual=false -Dsbt.server.autostart=false \
            -Dsbt.boot.directory="$out/sbt-boot" \
            -Dsbt.ivy.home="$out/ivy2" \
            "sbtRoot / update" \
            "lmCoursierShaded / update" \
            "dist / update" \
            "launcherPackage / update" \
            "launcherPackageIntegrationTest / update" \
            "community-build / update" \
            "exampleWorkProj / update" \
            "hashBenchmark / update"
          runHook postBuild
        '';
        installPhase = "true";
      };
      sbt = pkgs.stdenvNoCC.mkDerivation {
        pname = "sbt";
        inherit version;
        src = self;
        nativeBuildInputs = [ pkgs.sbt jdk pkgs.makeWrapper pkgs.cacert ];
        buildInputs = [ sbtDependencies ];

        buildPhase = ''
          runHook preBuild
          export HOME="$TMPDIR/home"
          mkdir -p "$HOME"
          cp -r ${sbtDependencies}/sbt-boot "$HOME/sbt-boot"
          cp -r ${sbtDependencies}/coursier "$HOME/coursier"
          if [ -d ${sbtDependencies}/ivy2 ]; then
            cp -r ${sbtDependencies}/ivy2 "$HOME/ivy2"
          else
            mkdir -p "$HOME/ivy2"
          fi
          chmod -R u+w "$HOME"
          export JAVA_HOME=${jdk}
          export CI=true
          export COURSIER_CACHE="$HOME/coursier"
          sbt --server -batch -Dsbt.io.virtual=false -Dsbt.server.autostart=false \
            -Dsbt.boot.directory="$HOME/sbt-boot" \
            -Dsbt.ivy.home="$HOME/ivy2" \
            "sbtRoot / publishLocalBinAll" \
            "bundledLauncherProj / Compile / packageBin"
          runHook postBuild
        '';

        installPhase = ''
          runHook preInstall
          mkdir -p "$out/bin" "$out/share/sbt/bin" "$out/share/sbt/ivy2"
          cp sbt "$out/share/sbt/bin/sbt"
          substituteInPlace "$out/share/sbt/bin/sbt" \
            --replace-fail 'init_sbt_version=_to_be_replaced' 'init_sbt_version=${version}'
          launcher=$(find target/out -type f -name sbt-launch.jar -print -quit)
          test -n "$launcher"
          cp "$launcher" "$out/share/sbt/bin/sbt-launch.jar"
          cp -r "$HOME/ivy2/local" "$out/share/sbt/ivy2/local"
          cat > "$out/share/sbt/repositories" <<EOF
          [repositories]
            local-sbt: file://$out/share/sbt/ivy2/local/, [organization]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext]
            maven-central: https://repo1.maven.org/maven2/
          EOF
          makeWrapper "$out/share/sbt/bin/sbt" "$out/bin/sbt" \
            --set JAVA_HOME ${jdk} \
            --prefix PATH : ${lib.makeBinPath [ jdk pkgs.coreutils pkgs.gnused pkgs.gnugrep pkgs.curl ]} \
            --add-flags "--server -Dsbt.repository.config=$out/share/sbt/repositories"
          runHook postInstall
        '';

        doInstallCheck = true;
        installCheckPhase = ''
          runHook preInstallCheck
          export HOME="$TMPDIR/install-home"
          mkdir -p "$HOME"
          "$out/bin/sbt" --version --allow-empty | grep -q "${version}"
          test -d "$out/share/sbt/ivy2/local/org.scala-sbt/sbt/${version}"
          cp -r ${sbtDependencies}/coursier "$HOME/coursier"
          chmod -R u+w "$HOME/coursier"
          export COURSIER_CACHE="$HOME/coursier"
          mkdir -p "$HOME/project"
          printf 'sbt.version=${version}\n' > "$HOME/project/build.properties"
          printf 'scalaVersion := "3.9.0"\n' > "$HOME/build.sbt"
          cd "$HOME"
          set -o pipefail
          "$out/bin/sbt" --no-colors -batch -Dsbt.server.autostart=false \
            "show sbtVersion" 2>&1 | tee "$TMPDIR/sbt-version.log"
          grep -Fxq "[info] ${version}" "$TMPDIR/sbt-version.log"
          runHook postInstallCheck
        '';

        meta = {
          description = "sbt built from source with a JVM launcher";
          homepage = "https://www.scala-sbt.org/";
          license = lib.licenses.asl20;
          mainProgram = "sbt";
          platforms = [ system ];
        };
      };
    in
    {
      packages.${system} = {
        inherit sbt;
        default = sbt;
      };
      devShells.${system}.default = pkgs.mkShellNoCC {
        packages = [ jdk pkgs.sbt ];
      };
    };
}
