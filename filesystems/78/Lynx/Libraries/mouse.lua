local lib = {}
local event = require("event")
local mouse = computer.components()[5]
lib.pressed = false
lib.x = 0
lib.y = 0

function lib.getX()
	return lib.x
end

function lib.isPressed()
	return lib.pressed
end

function lib.getY()
	return lib.y
end

-- Interrupt the program (the OS manages this) to collect last mouse informations
function lib.driverInterrupt()
	local i = mouse:receive()
	if i == 1 then
		lib.x = mouse:receive()
		lib.y = mouse:receive()
		event.fireEvent("mouseMoved", lib.x, lib.y)
		if lib.pressed then
			event.fireEvent("mouseDragged", lib.x, lib.y)
		end
	end
	if i == 2 then
		event.fireEvent("mousePressed", lib.x, lib.y)
		lib.pressed = true
	end
	if i == 3 then
		event.fireEvent("mouseReleased", lib.x, lib.y)
		lib.pressed = false
	end
end

return lib