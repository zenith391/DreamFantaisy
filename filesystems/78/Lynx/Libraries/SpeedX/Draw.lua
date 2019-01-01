-- SpeedX's SpeedDraw version 1

local lib = {}
local ops = require("SpeedX/Operations")
local gpul = require("gpu")
local gpu = gpu.getGPUComponent()

function lib.createContext(x, y, width, height)
	local ctx = {}
	ctx.buf = {}
	ctx.color = 0xFFFFFF
	ctx.fillRect = function(x, y, width, height)
		table.insert(ctx.buf, {52, x, y, width, height, ctx.color})
	end
	return ctx
end

function lib.flushContext(ctx)
	while table.maxn(ctx.buf) ~= 0 do
		local op = ctx.buf[1]
		ops.pushLLOperation(op)
	end
end

function lib.getAPIVersion()
	return 1.0
end

function lib.decodeImage(data)
	if gpu.hasMGE() then
		return 0, gpu.loadImage(data)
	else
		-- Manually load
	end
end
return lib