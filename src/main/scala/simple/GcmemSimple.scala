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
 * A simple, configurable counter that wraps around.
 */
class GcmemSimple() extends Module {
  val io = new Bundle {
    val read = Bool(INPUT)
    val rd = Bool(OUTPUT)
    val write = Bool(INPUT)
    val wr = Bool(OUTPUT)
    val moor_rd = Bool(OUTPUT)
    val out = UInt(OUTPUT, 1)
  }

  val idle :: read :: write :: Nil = Enum(UInt(), 3)
  val stateReg = Reg(init = idle)
  
  val TRUE = Bool(true)
  val FALSE = Bool(false)

  // Have a default assignment for combinational signals
  io.rd := Bool(false)
  io.wr := Bool(false)
  io.moor_rd := Bool(false)

  // A little bit verbosely written
  // Mealy FSM
  switch(stateReg) {
    is(idle) {
      io.rd := FALSE
      when(io.read) {
        stateReg := read
        io.rd := Bool(true)
      }.elsewhen(io.write) {
        stateReg := write
        io.wr := Bool(true)
      }
    }
    is(read) {
      io.rd := Bool(true)
      when(!io.read) {
        stateReg := idle
        io.rd := Bool(false)
      }

    }
    is(write) {
      io.wr := Bool(true)
      when(!io.write) {
        stateReg := idle
        io.wr := Bool(false)
      }
    }
  }

  // With a Moore FSM the output is one cycle delayed
  when(stateReg === idle) {
    io.moor_rd := Bool(false)
  }.elsewhen(stateReg === read) {
    io.moor_rd := Bool(true)
  }
}

object GcmemMain {
  def main(args: Array[String]): Unit = {
    chiselMain(Array("--backend", "v", "--targetDir", "generated"),
      () => Module(new GcmemSimple()))
  }
}