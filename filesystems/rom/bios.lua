local gpu = computer.components()[2]

gpu:sendDirect(52)
gpu:sendDirect(0)
gpu:sendDirect(0)
gpu:sendDirect(640)
gpu:sendDirect(480)
gpu:sendDirect(0x000000)
gpu:sendDirect(53)
local bootPath = computer.loadFromDrive(computer.getROMData(), "boot.mbf")
local bootCode = computer.loadFromDrive(computer.getROMData(), bootPath)

computer.sleep(1000)

local f, err = load(bootCode, "=" .. bootPath, "t")
if err ~= nil then
	print(debug.traceback())
	error(err)
end
if f ~= nil then
	f()
end