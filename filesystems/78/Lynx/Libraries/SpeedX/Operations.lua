-- Simple pipeline for graphics operations

local buffer = {}
local gpu = require("gpu")
local lib = {}

-- Pushes Low Level Operation
function lib.pushLLOperation(op)
	table.insert(buffer, op)
end

function lib.process()
	if table.maxn(buffer) ~= 0 then
		local fop = buffer[1]
		for k, v in pairs(fop)
			gpu.sendULLOperation(v)
		end
		table.remove(lib, 1)
		gpu.flushBuffer()
	end
end

return lib