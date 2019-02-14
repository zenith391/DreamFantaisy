local sandbox = _G
local s = {}
local prn = _G.print

local function makeSandbox()
	sandbox.assert = nil
	--sandbox.collectgarbage = nil
	sandbox.dofile = nil
	sandbox.loadfile = nil
	return sandbox
end

local gpu = computer.components()[2]
local c = coroutine.create(function()
	local ok, err = pcall(function()
		makeSandbox()
		local code, err = computer.loadFromROM("bios.lua")
		if code ~= nil then
			local f,err = load(code, "=bios.lua", "t")
			if f ~= nil then
				local ok2, err2 = xpcall(f, function(err)
					prn(err);
					prn(debug.traceback());
				end);
				if ok2 == false then
					print(err2)
					--print(err2)
					error(err2)
				end
			else
				error(err)
			end
		else
			error("Error while reading the BIOS: " .. err)
		end
	end)
	if ok == false then
		-- TODO handle error
		print("Handled error: ")
		print(err)
		--error(err)
	end
end)

return c