local gpu = computer.components()[3]
local kbd = computer.components()[2]


local bootPath = computer.loadFromDrive(computer.getROMData(), "boot.mbf")
local bootCode = computer.loadFromDrive(computer.getROMData(), bootPath)

gpu:text(0, 0, "DreamComputer's DreamBIOS rev 1", 0xFFFFFF)
gpu:text(0, 1, "Boot Drive: " .. computer.getROMData(), 0xFFFFFF)
gpu:text(0, 2, "Boot Path: " .. bootPath, 0xFFFFFF)
gpu:text(0, 3, "Full: " .. computer.getROMData() .. ":" .. bootPath, 0xFFFFFF)
gpu:text(0, 29, "Press P to access boot menu", 0xFFFFFF)
gpu:updateScreen()
computer.sleep(2000)
local ip = kbd:receive()
if ip == 1 then
	local cha = kbd:receive()
	local ch = kbd:receive()
	if ch == 77 then -- P pressed
		gpu:text(0, 5, "TEST")
		gpu:updateScreen()
		computer.sleep(2000)
	end
end

local f, err = load(bootCode, "=" .. bootPath, "t")
if err ~= nil then
	print(debug.traceback())
	error(err)
end
if f ~= nil then
	f()
end