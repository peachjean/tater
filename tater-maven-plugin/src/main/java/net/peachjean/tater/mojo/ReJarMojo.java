package net.peachjean.tater.mojo;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.ManifestException;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @goal rejar
 * @phase package
 */
public class ReJarMojo extends AbstractMojo {

    /**
     * @parameter expression="${project.basedir}/src/main/apt"
     * @required
     */
    private File aptDirectory;

    /**
     * Directory containing the generated JAR.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    /**
     * Name of the generated JAR.
     *
     * @parameter alias="jarName" expression="${jar.finalName}" default-value="${project.build.finalName}"
     * @required
     */
    private String finalName;

    /**
     * The Jar archiver.
     *
     * @component role="org.codehaus.plexus.archiver.Archiver" roleHint="jar"
     */
    private JarArchiver jarArchiver;

    /**
     * The archive configuration to use.
     * See <a href="http://maven.apache.org/shared/maven-archiver/index.html">Maven Archiver Reference</a>.
     *
     * @parameter
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

    /**
     * Whether creating the archive should be forced.
     *
     * @parameter expression="${jar.forceCreation}" default-value="false"
     */
    private boolean forceCreation;

    /**
     * Classifier to add to the artifact generated. If given, the artifact will be an attachment instead.
     *
     * @parameter
     */
    private String classifier;

    /**
     * The Maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    protected static File getJarFile( File basedir, String finalName, String classifier )
    {
        return getJarFile(basedir, finalName, classifier, "jar");
    }

    protected static File getJarFile( File basedir, String finalName, String classifier, String extension )
    {
        if ( classifier == null )
        {
            classifier = "";
        }
        else if ( classifier.trim().length() > 0 && !classifier.startsWith( "-" ) )
        {
            classifier = "-" + classifier;
        }

        return new File( basedir, finalName + classifier + "." + extension );
    }


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File jarFile = getJarFile( outputDirectory, finalName, classifier );

        File origJarFile = getJarFile(outputDirectory, finalName, classifier, "orig.jar");
        try {
            FileUtils.rename(jarFile, origJarFile);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to copy original jar file to backup location.", e);
        }

        MavenArchiver archiver = new MavenArchiver();

        archiver.setArchiver( jarArchiver );

        archiver.setOutputFile( jarFile );

        archive.setForced( forceCreation );

        try {
            archiver.getArchiver().addArchivedFileSet(origJarFile);
            archiver.getArchiver().addDirectory(aptDirectory);
            archiver.createArchive(project, archive);
        } catch (ArchiverException e) {
            throw new MojoExecutionException("Failed to construct archiver.", e);
        } catch (ManifestException e) {
            throw new MojoExecutionException("Failed to construct archiver.", e);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to construct archiver.", e);
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Failed to construct archiver.", e);
        }

    }
}
