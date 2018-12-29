local sandbox = _G

function makeSandbox()
	sandbox.assert = nil
	sandbox.collectgarbage = nil
	sandbox.dofile = nil
	sandbox.loadfile = nil
	--sandbox.print = nil
	return sandbox
end

	local box = makeSandbox()
	local gpu = computer.components()[2]
	local c = coroutine.create(function()
		local ok, err = pcall(function()
			local code, err = computer.loadFromROM("bios.lua")
			if code ~= nil then
				local f,err = load(code, "=bios.lua", "t")
				if f ~= nil then
					local ok2, err2 = pcall(f);
					if ok2 == false then
						print("an error occured: " .. err2)
						print(debug.traceback())
						error(0)
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
			print(err)
			error(err)
		end
	end)

return c