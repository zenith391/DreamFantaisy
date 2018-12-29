local gpu = computer.components()[2]

gpu:sendDirect(52)
gpu:sendDirect(0)
gpu:sendDirect(0)
gpu:sendDirect(1280)
gpu:sendDirect(720)
gpu:sendDirect(0x000000)
gpu:sendDirect(53)
--print(computer.getROMData())
local bootPath = computer.loadFromDrive(computer.getROMData(), "boot.mbf")
--print("mbf path: " .. bootPath)
local bootCode = computer.loadFromDrive(computer.getROMData(), bootPath)

local f, err = load(bootCode, "=" .. bootPath, "t")
if err ~= nil then
	print(debug.traceback())
	error(err)
end
if f ~= nil then
	f()
end