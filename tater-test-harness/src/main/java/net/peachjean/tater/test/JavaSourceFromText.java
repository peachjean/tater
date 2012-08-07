package net.peachjean.tater.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

import javax.tools.SimpleJavaFileObject;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class JavaSourceFromText extends SimpleJavaFileObject
{
	/**
	 * The source code of this "file".
	 */
	final String code;

	/**
	 * Constructs a new JavaSourceFromString.
	 *
	 * @param name the name of the compilation unit represented by this file object
	 * @param code the source code for the compilation unit represented by this file object
	 */
	public JavaSourceFromText(String name, String code)
	{
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),
		      Kind.SOURCE);
		this.code = code;
	}

	public JavaSourceFromText(String name, File code) throws IOException
	{
		this(name, Files.toString(code, Charset.defaultCharset()));
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors)
	{
		return code;
	}

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder {

        private final String name;
        private final List<String> lines = Lists.newArrayList();

        public Builder(String name) {
            this.name = name;
        }

        public Builder line(final String line) {
            this.lines.add(line);
            return this;
        }

        public JavaSourceFromText build() {
            return new JavaSourceFromText(this.name, Joiner.on("\n").join(lines));
        }
    }
}
