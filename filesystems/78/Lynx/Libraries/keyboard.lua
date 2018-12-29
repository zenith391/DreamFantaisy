local lib = {}
local event = require("event")
lib.keys = {}
local keyboard = computer.components()[1]
lib.maj = false
lib.ctrl = false

function lib.isMajPressed()
	return lib.maj
end

function lib.isCtrlPressed()
	return lib.ctrl
end

-- Convert driver names to readable character.
function lib.toTypedChar(cp)
	if lib.isMajPressed() then
		cp = cp:upper()
	end
	if cp == "space" then
		cp = string.char(32)
	end
	return ch
end

-- Interrupt the program (the OS manages this) to collect last keyboard informations
function lib.driverInterrupt()
	local i = keyboard:receive()
	if i ~= 0 then
		local j = keyboard:receive()
		local ch = keyboard:receive()
		if i == 1 then
			event.fireEvent("keyPressed", j, ch)
			if j == 16 then
				lib.maj = true
			elseif j == 17 then
				lib.ctrl = true
			end
		end
		if i == 2 then
			event.fireEvent("keyReleased", j, ch)
			if j == 16 then
				lib.maj = false
			elseif j == 17 then
				lib.ctrl = false
			end
		end
	end
end

return lib