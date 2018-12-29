local lib = {}
local listeners = {}

function lib.fireEvent(name, ...)
	for k, v in pairs(listeners) do
		if v.name == name or v.name == "*" then
			v.exec(name, ...)
		end
	end
end

function lib.register(name, listener)
	local lis = {}
	lis.name = name
	lis.exec = listener
	table.insert(listeners, lis)
end

function lib.cancel(name, listener)
	for k, v in pairs(listeners) do
		if v.name == name and v.exec == listener then
			table.remove(listeners, k)
			return true
		end
	end
	return false
end

return lib