#
# 

SBT = sbt

# Generate the C++ simulation and run the tests

gcmem:
	$(SBT) "run-main simple.GcmemTester"


view:
	gtkwave generated/GcmemSimple.vcd # --save=abc.gtkw


# Generate Verilog code

gcmem-gen:
	$(SBT) "run-main simple.GcmemMain"

