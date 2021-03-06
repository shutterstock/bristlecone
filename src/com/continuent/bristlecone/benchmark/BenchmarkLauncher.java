/**
 * Bristlecone Test Tools for Databases
 * Copyright (C) 2006-2007 Continuent Inc.
 * Contact: bristlecone@lists.forge.continuent.org
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA
 *
 * Initial developer(s): Robert Hodges and Ralph Hannus.
 * Contributor(s):
 */

package com.continuent.bristlecone.benchmark;

import org.apache.log4j.Logger;

/**
 * Launcher for benchmark tests.  This class parses command line arguments and 
 * invokes the test. 
 * 
 * @author <a href="mailto:robert.hodges@continuent.com">Robert Hodges</a>
 * @version 1.0
 */
public class BenchmarkLauncher
{
  private static Logger logger = Logger.getLogger(BenchmarkLauncher.class);

  /** Creates a new Benchmark instance. */
  public BenchmarkLauncher()
  {
  }

  /** Main method to permit external invocation. */
  public static void main(String argv[]) throws Exception
  {
    String props = null;
    String csv = null;
    String text = null;
    String html = null;

    // Parse arguments.
    int argc = 0;
    while (argc < argv.length)
    {
      String nextArg = argv[argc];
      argc++;
      if ("-props".equals(nextArg))
      {
        props = argv[argc++];
      }
      else if ("-text".equals(nextArg))
      {
        text = argv[argc++];
      }
      else if ("-csv".equals(nextArg))
      {
        csv = argv[argc++];
      }
      else if ("-html".equals(nextArg))
      {
        html = argv[argc++];
      }
      else if ("-help".equals(nextArg))
      {
        usage();
        return;
      }
      else
      {
        String msg = "Unrecognized flag (try -help for usage): " + nextArg;
        println(msg);
        exitWithFailure();
      }
    }

    // Run the benchmark.
    try
    {
      logger.info("Setting benchmark control parameters");
      Benchmark benchmark = new Benchmark();
      if (props != null)
        benchmark.setProps(props);
      if (text != null)
        benchmark.setText(text);
      if (csv != null)
        benchmark.setCsv(csv);
      if (html != null)
        benchmark.setHtml(html);

      logger.info("Starting benchmark");
      benchmark.go();
    }
    catch (Throwable t)
    {
      logger.fatal("Benchmark execution failed due to unexpected exception", t);

      // Catch and print the error that caused benchmark failure.
      println("Benchmark execution failed...See log for detailed stack trace(s)");
      println("EXCEPTION: " + t.getMessage());

      // Print out sub-exceptions as well.
      Throwable cause = t;
      while ((cause = cause.getCause()) != null)
      {
        println("SUB-EXCEPTION: " + cause.getMessage());
      }
    }
  }

  /** Print to standard out. */
  protected static void println(String message)
  {
    System.out.println(message);
  }

  /** Print usage. */
  protected static void usage()
  {
    println("Usage: java " + BenchmarkLauncher.class.getName() + " options ");
    println("  -props propsfile  Scenario properties file (default=benchmark.properties");
    println("  -text file        Log results as text report in 'file'");
//    println("  -graph            Log results to dynamically generated graph");
    println("  -csv file         Log results as CSV in 'file'");
    println("  -html file        Log results as HTML report in 'file'");
    println("  -help             Print usage");
    println("Properties file must have at least scenario, url, and user values to run");
    println("Multiple output formats may be selected.  The default is log messages only");
  }

  // Fail gloriously.
  protected static void exitWithFailure()
  {
    System.exit(1);
  }

  // Exit with a success code.
  protected static void exitWithSuccess1()
  {
    System.exit(0);
  }
}