package org.apache.camel.karavan.cli;

import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "install",
        mixinStandardHelpOptions = true,
        description = "Install Karavan")
public class InstallCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-v", "--version"}, required = true, description = "Karavan version", defaultValue = "3.21.0")
    private String version;
    @CommandLine.Option(names = {"-n", "--namespace"}, description = "Namespace", defaultValue = Constants.DEFAULT_NAMESPACE)
    private String namespace;
    @CommandLine.Option(names = {"-e", "--environment"}, description = "Environment", defaultValue = Constants.DEFAULT_ENVIRONMENT)
    private String environment;
    @CommandLine.Option(names = {"-r", "--runtimes"}, description = "Runtimes: quarkus, spring-boot", defaultValue = Constants.DEFAULT_RUNTIMES)
    private String runtimes;
    @CommandLine.Option(names = {"--auth"}, description = "Authentication: public, basic, oidc", defaultValue = Constants.DEFAULT_AUTH)
    private String auth;
    @CommandLine.Option(names = {"--node-port"}, description = "Node port", defaultValue = "0")
    private int nodePort;
    @CommandLine.Option(names = {"--instances"}, description = "Instances. Default: 1", defaultValue = "1")
    private int instances;
    @CommandLine.Option(names = {"--base-image"}, description = "Base Image", defaultValue = Constants.KARAVAN_IMAGE)
    private String baseImage;
    @CommandLine.Option(names = {"--base-builder-image"}, description = "Base Builder Image", defaultValue = Constants.DEFAULT_BUILD_IMAGE)
    private String baseBuilderImage;
    @CommandLine.Option(names = {"--file"}, description = "YAML file name", defaultValue = "karavan.yaml")
    private String file;
    @CommandLine.Option(names = {"--yaml"}, description = "Create YAML file. Do not apply")
    private boolean yaml;
    @CommandLine.Option(names = {"--openshift"}, description = "Create files for OpenShift")
    private boolean isOpenShift;

    @CommandLine.Option(names = {"--master-password"}, description = "Master password", defaultValue = "karavan")
    private String masterPassword;
    @CommandLine.Option(names = {"--oidc-secret"}, description = "OIDC secret")
    private String oidcSecret;
    @CommandLine.Option(names = {"--oidc-server-url"}, description = "OIDC server URL")
    private String oidcServerUrl;
    @CommandLine.Option(names = {"--oidc-frontend-url"}, description = "OIDC frontend URL")
    private String oidcFrontendUrl;
    @CommandLine.Option(names = {"--git-repository"}, description = "Git repository")
    private String gitRepository;
    @CommandLine.Option(names = {"--git-username"}, description = "Git username")
    private String gitUsername;
    @CommandLine.Option(names = {"--git-password"}, description = "Git password")
    private String gitPassword;
    @CommandLine.Option(names = {"--git-branch"}, description = "Git branch", defaultValue = "main")
    private String gitBranch;
    @CommandLine.Option(names = {"--git-pull"}, description = "Git pull interval. Default: off", defaultValue = "off")
    private String gitPullInterval;
    @CommandLine.Option(names = {"--image-registry"}, description = "Image registry")
    private String imageRegistry;
    @CommandLine.Option(names = {"--image-group"}, description = "Image group", defaultValue = "karavan")
    private String imageGroup;
    @CommandLine.Option(names = {"--image-registry-username"}, description = "Image registry username")
    private String imageRegistryUsername;
    @CommandLine.Option(names = {"--image-registry-password"}, description = "Image registry password")
    private String imageRegistryPassword;

    @CommandLine.Option(names = {"--nexus-proxy"}, description = "Deploy nexus proxy")
    private boolean nexusProxy;

    @CommandLine.Option(names = { "-h", "--help" }, usageHelp = true, description = "Display help")
    private boolean helpRequested;

    @Override
    public Integer call() throws Exception {
        KaravanConfig config = new KaravanConfig (
                version,
                namespace,
                environment,
                runtimes,
                auth,
                nodePort,
                instances,
                baseImage,
                baseBuilderImage,
                isOpenShift,
                new HashMap<>(),
                masterPassword,
                oidcSecret,
                oidcServerUrl,
                oidcFrontendUrl,
                gitRepository,
                gitUsername,
                gitPassword,
                gitBranch,
                gitPullInterval,
                imageRegistry,
                imageGroup,
                imageRegistryUsername,
                imageRegistryPassword,
                nexusProxy
        );
        if (yaml) {
            Files.writeString(Path.of(file), ResourceUtils.generateResources(config));
        } else {
            CommandUtils.installKaravan(config);
        }
        return 0;
    }
}
