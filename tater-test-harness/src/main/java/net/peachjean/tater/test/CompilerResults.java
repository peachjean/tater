package net.peachjean.tater.test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.peachjean.commons.test.junit.AssertionHandler;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.annotation.Nullable;
import javax.tools.*;

public class CompilerResults
{
	private final boolean successful;
	private final String compilerOutput;
	private final List<Diagnostic<? extends JavaFileObject>> diagnostics;
    private final AssertionHandler assertionHandler;
    private final StandardJavaFileManager fileManager;

    CompilerResults(final boolean successful, final String compilerOutput,
                    final List<Diagnostic<? extends JavaFileObject>> diagnostics,
                    AssertionHandler assertionHandler, StandardJavaFileManager fileManager) {
		this.successful = successful;
		this.compilerOutput = compilerOutput;
		this.diagnostics = diagnostics;
        this.assertionHandler = assertionHandler;
        this.fileManager = fileManager;
    }

	public boolean isSuccessful()
	{
		return successful;
	}

	public String getCompilerOutput()
	{
		return compilerOutput;
	}

	public List<Diagnostic<? extends JavaFileObject>> getDiagnostics()
	{
		return diagnostics;
	}

    public Collection<Diagnostic<? extends JavaFileObject>> getDiagnosticsOfKind(final Diagnostic.Kind kind)
    {
        return Collections2.filter(this.getDiagnostics(), new Predicate<Diagnostic<? extends JavaFileObject>>() {
            @Override
            public boolean apply(@Nullable Diagnostic<? extends JavaFileObject> input) {
                return input.getKind() == kind;
            }
        });
    }

    public void assertNoOutput() {
        this.assertionHandler.assertEquals("Unexpected Compiler Output", "", this.getCompilerOutput());
    }

    public void assertNumberOfDiagnostics(Diagnostic.Kind kind, int i) {
        this.assertionHandler.assertEquals("Unexpected number of " + kind + " diagnostics.", i,
                this.getDiagnosticsOfKind(kind).size());
    }

    public void assertDiagnosticMatches(Diagnostic.Kind kind, String patternString, int numExpected) {
        int numFound = 0;
        Pattern pattern = Pattern.compile(patternString);
        for(Diagnostic<? extends JavaFileObject> diagnostic: this.getDiagnosticsOfKind(kind)) {
            final String message = diagnostic.getMessage(Locale.getDefault());
            if(pattern.matcher(message).find()) {
                numFound++;
            }
        }
        this.assertionHandler.assertEquals("Unexpected number of diagnostics matching pattern [[ " + patternString + " ]].",
                numExpected, numFound);
    }

    /**
     * Allows running assertions from within the undertest codebase.
     *
     * @param asserterClass The name of the class from the undertest codebase that implements {@link CompilerAsserter}.
     * @throws ClassNotFoundException
     */
    public void runAssertion(String asserterClass) throws ClassNotFoundException {
        ClassLoader cl = fileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
        Class<? extends CompilerAsserter> asserterType = (Class<? extends CompilerAsserter>) cl.loadClass(asserterClass);
        try {
            CompilerAsserter asserter = asserterType.newInstance();
            asserter.doAssertions(this.assertionHandler);
        } catch (InstantiationException e) {
            this.assertionHandler.fail(e.getMessage());
        } catch (IllegalAccessException e) {
            this.assertionHandler.fail(e.getMessage());
        }
    }
}
