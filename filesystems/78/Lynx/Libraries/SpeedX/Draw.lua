-- SpeedX's SpeedDraw version 1

local lib = {}
local oplib = require("SpeedX/Operations")
local gpul = require("gpu")
local gpu = gpul.getGPUComponent()

--/ Create a GPU accelerated context /--
function lib.createAcceleratedContext(x, y, width, height)
	local ctx = {}
	ctx.buf = {}
	ctx.color = 0xFFFFFF
	ctx.fillRect = function(x, y, width, height)
		table.insert(ctx.buf, {52, x, y, width, height, ctx.color})
	end
	ctx.plotPixel = function(x, y)
		ctx.fillRect(x, y, 1, 1)
	end
	ctx.drawImage = function(x, y, data) -- happens instantly
		gpu:drawImage(x, y, data)
	end
	return ctx
end

--/	Create a raster (bitmap) context, the data argument should be the decoded data and not the actual
--	GPU id of the image. Raster contexts are directly writed, they doesn't need being flushed.
--	To get the updated image, it must be accessed from ctx.data
--	Note: RGB or RGBA colors depends on the number of bits in the ctx.color field. /--
function lib.createRasterContext(data)
	local ctx = {}
	local iwidth = data[1]
	local iheight = data[2]
	ctx.data = data
	ctx.buf = {}
	ctx.color = 0xFFFFFF
	ctx.fillRect = function(x, y, width, height)
		local x1 = x
		local y1 = y
		while x1 < x + width do
			y1 = y
			while y1 < y + height do
				ctx.plotPixel(x1, y1)
				y1 = y1 + 1
			end
			x1 = x1 + 1
		end
	end
	ctx.plotPixel = function(x, y)
		data[3 + x * iheight + 3] = ctx.color
	end
	return ctx
end

function lib.flushContext(ctx)
	while table.maxn(ctx.buf) ~= 0 do
		local op = ctx.buf[1]
		oplib.pushLLOperation(op)
		table.remove(ctx.buf, 1)
	end
end

function lib.getAPIVersion()
	return 1.0
end

function lib.newImage(width, height)
	local data = {}
	data[1] = width
	data[2] = height
	local i = 3
	while i < width * height + 5 do
		data[i] = 0x000000
		i = i + 1
	end
	osdbg("image created")
	return data
end

function lib.decodeImage(data)
	if gpu:hasMGE() then
		return 0, gpu:loadImage(data)
	else
		-- Manually load
	end
end
return lib