/*
 * Copyright: 2016, Technical University of Denmark, DTU Compute
 * Author: Martin Schoeberl (martin@jopdesign.com)
 * License: Simplified BSD License
 * 
 * Playground for gcmem experiments.
 * 
 */

package simple

import Chisel._


/**
 * Test the counter by printing out the value at each clock cycle.
 */
class GcmemTester(c: GcmemSimple) extends Tester(c) {

  poke(c.io.read, 0)
  peek(c.io.rd)
  peek(c.io.moor_rd)
  step(1)
  poke(c.io.read, 1)
  peek(c.io.rd)
  peek(c.io.moor_rd)
  step(1)
  poke(c.io.read, 1)
  peek(c.io.rd)
  peek(c.io.moor_rd)
  step(1)
  poke(c.io.read, 0)
  peek(c.io.rd)
  peek(c.io.moor_rd)
  step(1)
  poke(c.io.read, 0)
  peek(c.io.rd)
  peek(c.io.moor_rd)
  step(1)
  
}

/**
 * Create component a tester.
 */
object GcmemTester {
  def main(args: Array[String]): Unit = {
    chiselMainTest(Array("--genHarness", "--test", "--backend", "c",
      "--compile", "--vcd", "--targetDir", "generated"),
      () => Module(new GcmemSimple())) {
        c => new GcmemTester(c)
      }
  }
}