-- Simple pipeline for graphics operations

local buffer = {}
local gpu = require("gpu")
local lib = {}

-- Pushes Low Level Operation
function lib.pushLLOperation(op)
	table.insert(lib, op)
end

function lib.process()
	if table.maxn(buffer) ~= 0 then
		local fop = buffer[1]
		for k, v in pairs(fop)
			
		end
		table.remove(lib, 1)
	end
end

return lib