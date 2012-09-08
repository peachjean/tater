package net.peachjean.tater.test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.peachjean.commons.test.junit.AssertionHandler;

public class CompilerHarness
{
	private final File rootOutput;
	private final JavaFileObject[] sourceFiles;

    private final AssertionHandler assertionHandler;

	private List<Processor> processors = Lists.newArrayList();
	private File classesDirectory;
	private File sourceDirectory;

    public CompilerHarness(final File rootOutput, final JavaFileObject... sourceFiles)
    {
        this(rootOutput, AssertionHandler.DEFAULT, sourceFiles);
    }

	public CompilerHarness(final File rootOutput, final AssertionHandler assertionHandler,
                           final JavaFileObject... sourceFiles)
	{
		this.rootOutput = rootOutput;
        this.assertionHandler = assertionHandler;
		this.sourceFiles = sourceFiles;
		this.classesDirectory = new File(rootOutput, "classes");
		classesDirectory.mkdirs();
		this.sourceDirectory = new File(rootOutput, "sources");
		sourceDirectory.mkdirs();
	}

	public File getClassesDirectory()
	{
		return classesDirectory;
	}

	public File getSourceDirectory()
	{
		return sourceDirectory;
	}

	public CompilerHarness addProcessor(Processor processor)
	{
		this.processors.add(processor);
		return this;
	}

	public CompilerResults invoke() throws IOException
	{

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		final Iterable<? extends JavaFileObject> compilationUnits = Lists.newArrayList(sourceFiles);

		StringWriter compilerOutput = new StringWriter();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(classesDirectory));
		fileManager.setLocation(StandardLocation.SOURCE_OUTPUT, Arrays.asList(sourceDirectory));

		JavaCompiler.CompilationTask compilerTask =
				compiler.getTask(compilerOutput, fileManager, diagnostics,
                        ImmutableList.of("-Xlint:unchecked", "-g"), null, compilationUnits);
		compilerTask.setProcessors(processors);

		boolean success = compilerTask.call();

		compilerOutput.close();
		fileManager.close();
		return new CompilerResults(success, compilerOutput.toString(), diagnostics.getDiagnostics(), assertionHandler, fileManager);
	}

}
