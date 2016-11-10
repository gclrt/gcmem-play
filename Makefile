#
# 

SBT = sbt

# Generate the C++ simulation and run the tests

gcmem:
	$(SBT) "test:runMain simple.GcmemTester"

#	$(SBT) "run-main simple.GcmemTester"


view:
	gtkwave generated/GcmemSimple.vcd # --save=abc.gtkw


# Generate Verilog code

gen:
	$(SBT) "run-main simple.GcmemMain"

