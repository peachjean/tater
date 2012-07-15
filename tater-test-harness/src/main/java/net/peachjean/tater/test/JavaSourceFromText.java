package net.peachjean.tater.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

import javax.tools.SimpleJavaFileObject;

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
}
