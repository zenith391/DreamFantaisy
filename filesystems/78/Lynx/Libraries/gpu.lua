--%/libtype=SYSTEM

local lib = {}
local color = 0x000000
local gpu = computer.components()[2]
local currentMode = 1

function lib.setColor(rgbColor)
	color = rgbColor
end

function lib.getColor()
	return color
end

function lib.switchToConsole()
	if currentMode == 1 then
		gpu:sendDirect(55)
		currentMode = 0
	end
end

function lib.switchToVideo()
	if currentMode == 0 then
		gpu:sendDirect(56)
		currentMode = 1
	end
end

function lib.flushBuffer()
	gpu:sendDirect(53)
end

function lib.fillRect(x, y, width, height)
	gpu:sendDirect(52)
	gpu:sendDirect(x)
	gpu:sendDirect(y)
	gpu:sendDirect(width)
	gpu:sendDirect(height)
	gpu:sendDirect(color)
end

-- Ultra Low Level operation
function lib.sendULLOperation(num)
	gpu:sendDirect(num)
end

function lib.drawText(x, y, text)
	gpu:sendDirect(54)
	gpu:sendDirect(x)
	gpu:sendDirect(y)
	gpu:sendDirect(color)
	local i = 1
	while i < string.len(text)+1 do
		gpu:sendDirect(string.byte(string.sub(text, i, i+1)))
		i = i + 1
	end
	gpu:sendDirect(0) -- null byte
end

return lib