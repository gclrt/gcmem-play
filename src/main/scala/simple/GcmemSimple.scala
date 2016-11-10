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
    val moor_rd = Bool(OUTPUT)
    val out = UInt(OUTPUT, 1)
  }

  val idle :: read :: Nil = Enum(UInt(), 2)
  val stateReg = Reg(init = idle)

  // Have a default assignment for combinational signals
  io.rd := Bool(false) 
  io.moor_rd := Bool(false)
  
  // A little bit verbosely written
  // Mealy FSM
  
  when(stateReg === idle) {
    io.rd := Bool(false)
    when(io.read) {
      stateReg := read
      io.rd := Bool(true)
    }
  }.elsewhen(stateReg === read) {
    io.rd := Bool(true)
    when(!io.read) {
      stateReg := idle
      io.rd := Bool(false)
    }
  }.otherwise {
    // There should not be an otherwise state
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