package org.luaj.vm2;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.luaj.vm2.lib.BaseLib;
import org.luaj.vm2.lib.DebugLib;
import org.luaj.vm2.lib.IoLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.ResourceFinder;

public class Globals extends LuaTable {

	
	public InputStream STDIN  = null;

	
	public PrintStream STDOUT = System.out;

	
	public PrintStream STDERR = System.err;

	
	public ResourceFinder finder;
	
	
	public LuaThread running = new LuaThread(this);

	
	public BaseLib baselib;
	
	
	public PackageLib package_;
	
	
	public DebugLib debuglib;

	
	public interface Loader {
		
		LuaFunction load(Prototype prototype, String chunkname, LuaValue env) throws IOException;
	}

	
	public interface Compiler {
		
		Prototype compile(String stream, String chunkname) throws IOException;
	}

	
	public interface Undumper {
		
		Prototype undump(String stream, String chunkname) throws IOException;
	}
	
	
	public Globals checkglobals() {
		return this;
	}
	
	/** The installed loader. 
	 * @see Loader */
	public Loader loader;

	/** The installed compiler.
	 * @see Compiler */
	public Compiler compiler;

	
	/** The installed undumper.
	 * @see Undumper */
	public Undumper undumper;

	/** Convenience function for loading a file that is either binary lua or lua source.
	 * @param filename Name of the file to load.
	 * @return LuaValue that can be call()'ed or invoke()'ed.
	 * @throws LuaError if the file could not be loaded.
	 */
	public LuaValue loadfile(String filename) {
		try {
			String script = "";
			for(String s : Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8)){
				script += s + "\r\n";
			}
			return load(script, "@"+filename, "bt", this);
		} catch (Exception e) {
			return error("load "+filename+": "+e);
		}
	}


	
	/** Convenience function to load a string value as a script.  Must be lua source.
	 * @param script Contents of a lua script, such as "print 'hello, world.'"
	 * @return LuaValue that may be executed via .call(), .invoke(), or .method() calls.
	 * @throws LuaError if the script could not be compiled.
	 */
	public LuaValue load(String script) {
		return load(script, script);
	}

	/** Convenience function to load a string value as a script with a custom environment.
	 * Must be lua source.
	 * @param script Contents of a lua script, such as "print 'hello, world.'"
	 * @param chunkname Name that will be used within the chunk as the source.
	 * @param environment LuaTable to be used as the environment for the loaded function.
	 * @return LuaValue that may be executed via .call(), .invoke(), or .method() calls.
	 * @throws LuaError if the script could not be compiled.
	 */
	public LuaValue load(String script, String chunkname, LuaTable environment) {
		return load(script, chunkname, environment);
	}

	/** Load the content form a reader as a text file.  Must be lua source. 
	 * The source is converted to UTF-8, so any characters appearing in quoted literals 
	 * above the range 128 will be converted into multiple bytes.  
	 * @param reader Reader containing text of a lua script, such as "print 'hello, world.'"
	 * @param chunkname Name that will be used within the chunk as the source.
	 * @return LuaValue that may be executed via .call(), .invoke(), or .method() calls.
	 * @throws LuaError if the script could not be compiled.
	 */ 
	public LuaValue load(String reader, String chunkname) {
		return load(reader, chunkname, "t", this);
	}

//	/** Load the content form a reader as a text file, supplying a custom environment.  
//	 * Must be lua source. The source is converted to UTF-8, so any characters 
//	 * appearing in quoted literals above the range 128 will be converted into
//	 * multiple bytes. 
//	 * @param reader Reader containing text of a lua script, such as "print 'hello, world.'"
//	 * @param chunkname Name that will be used within the chunk as the source.
//	 * @param environment LuaTable to be used as the environment for the loaded function.
//	 * @return LuaValue that may be executed via .call(), .invoke(), or .method() calls.
//	 * @throws LuaError if the script could not be compiled.
//	 */ 
//	public LuaValue load(Reader reader, String chunkname, LuaTable environment) {
//		return load(new UTF8Stream(reader), chunkname, "t", environment);
//	}	
//
	/** Load the content form an input stream as a binary chunk or text file. 
	 * @param is InputStream containing a lua script or compiled lua"
	 * @param chunkname Name that will be used within the chunk as the source.
	 * @param mode String containing 'b' or 't' or both to control loading as binary or text or either.
	 * @param environment LuaTable to be used as the environment for the loaded function.
	 * */
	public LuaValue load(String is, String chunkname, String mode, LuaValue environment) {
		try {
			Prototype p = loadPrototype(is, chunkname, mode);
			return loader.load(p, chunkname, environment);
		} catch (LuaError l) {
			throw l;
		} catch (Exception e) {
			return error("load "+chunkname+": "+e);
		}
	}

//	/** Load lua source or lua binary from an input stream into a Prototype. 
//	 * The InputStream is either a binary lua chunk starting with the lua binary chunk signature, 
//	 * or a text input file.  If it is a text input file, it is interpreted as a UTF-8 byte sequence.  
//	 * @param is Input stream containing a lua script or compiled lua"
//	 * @param chunkname Name that will be used within the chunk as the source.
//	 * @param mode String containing 'b' or 't' or both to control loading as binary or text or either.
//	 */
	public Prototype loadPrototype(String is, String chunkname, String mode) throws IOException {
		if (mode.indexOf('b') >= 0) {
			if (undumper == null)
				error("No undumper.");
			final Prototype p = undumper.undump(is, chunkname);
			if (p != null) 
				return p;
		}
		if (mode.indexOf('t') >= 0) {
			return compilePrototype(is, chunkname);
		}
		error("Failed to load prototype "+chunkname+" using mode '"+mode+"'");
		return null;
	}
//	
//	/** Compile lua source from a Reader into a Prototype. The characters in the reader 
//	 * are converted to bytes using the UTF-8 encoding, so a string literal containing 
//	 * characters with codepoints 128 or above will be converted into multiple bytes. 
//	 */
//	public Prototype compilePrototype(String reader, String chunkname) throws IOException {
//		return compilePrototype(reader, chunkname);
//	}
	
//	/** Compile lua source from an InputStream into a Prototype. 
//	 * The input is assumed to be UTf-8, but since bytes in the range 128-255 are passed along as 
//	 * literal bytes, any ASCII-compatible encoding such as ISO 8859-1 may also be used.  
//	 */
	public Prototype compilePrototype(String stream, String chunkname) throws IOException {
		if (compiler == null)
			error("No compiler.");
		return compiler.compile(stream, chunkname);
	}

	/** Function which yields the current thread. 
	 * @param args  Arguments to supply as return values in the resume function of the resuming thread.
	 * @return Values supplied as arguments to the resume() call that reactivates this thread.
	 */
	public Varargs yield(Varargs args) {
		if (running == null || running.isMainThread())
			throw new LuaError("cannot yield main thread");
		final LuaThread.State s = running.state;
		return s.lua_yield(args);
	}

}
