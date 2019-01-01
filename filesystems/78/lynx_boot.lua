_G.OSDATA = {}
_G.OSDATA.NAME = "LynxOS"
_G.OSDATA.VERSION = "0.1"

local loaded = {}
local run = true

function require(lib)
	if loaded[lib] ~= nil then
		return loaded[lib]
	end
	local err, lb = dofile("Lynx/Libraries/" .. lib .. ".lua")
	if err ~= nil then
		printerr("Could not load library: " .. err)
	end
	loaded[lib] = lb
	return loaded[lib]
end

function loadfile(file, drive)
	if drive == nil then
		drive = computer.getBootAddress()
	end
	local filec, err = computer.loadFromDrive(drive, file)
	if err ~= nil then
		return nil, err
	end
	local func, err1 = load(filec, "=" .. file, "t")
	if err1 ~= nil then
		--debug("Error:" .. err1)
		return nil, err
	end
	return func, nil
end

function dofile(file, drive)
	local f, err = loadfile(file, drive)
	if err ~= nil or f == nil then
		return err, nil
	end
	return nil, f()
end

function table.getn(table)
  local i = 0
  for k, v in pairs(table) do
    if k > i then
      i = k
    end
  end
  return i
end

function computer.getBootAddress()
	return computer.getROMData()
end

function computer.exists(path, drive)
	local f = loadfile(path, drive)
	return (f ~= nil)
end

function computer.shutdown()
	run = false
end

_G.osdbg = print
--_G.print = function() end
osdbg("[DEBUG] Mapped _G.print as _G.osdbg")

local gpu = require("gpu")
local gop = require("SpeedX/Operations")
local cube, cube_name, cube_gmode = dofile("Users/Root/Screensavers/moving_cube.lua")
local kbd = require("keyboard")
local ms = require("mouse")

osdbg("[DEBUG] LynxOS booting...")

osdbg("[DEBUG] Loading CLI at \"Lynx/system/cmd.itf\"")
local err, citfboot, citfupdate, citfquit = dofile("Lynx/system/cmd.itf")
osdbg("[DEBUG] LynxOS booted!")
osdbg("[DEBUG] Booting CLI")
citfboot()

local timeWithoutAction = 0
local function resetTime()
	timeWithoutAction = 0
end

require("event").register("keyPressed", resetTime)
require("event").register("mouseMoved", resetTime)

local ok, err = pcall(function()
	while run do
		if timeWithoutAction > 1280 then
			if cube_gmode == 1 then
				gpu.switchToConsole()
			end
			if cube_gmode == 2 then
				gpu.switchToVideo()
			end
			--cube(gpu)
		else
			gpu.switchToConsole()
			citfupdate()
		end
		-- event processing
		kbd.driverInterrupt()
		ms.driverInterrupt()
		if gop ~= nil then -- 
			gop.process()
		end
		timeWithoutAction = timeWithoutAction + 1
		computer.sleep(16) -- around 60 iterations per second
	end
end)
if not ok then
	gpu.setColor(0xFF0000)
	gpu.drawText(31, 11, "/-=-=-=-=-=-=-=-=\\")
	gpu.drawText(31, 12, "|Guru Meditation!|")
	gpu.drawText(31, 13, "|                |")
	gpu.drawText(31, 14, "\\=-=-=-=-=-=-=-=-/")
	gpu.flushBuffer()
	error(err)
end