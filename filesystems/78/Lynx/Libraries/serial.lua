local lib = {}

function lib.forPort(port)
	local obj = {}
	obj.cp = computer.components()[port]
	obj.write = function(ch)
		obj.cp:sendDirect(2); -- enable output mode
		obj.cp:sendDirect(ch); -- writes character
	end
	
	obj.isReadAvailable = function()
		obj.cp:sendDirect(5); -- tell it's reading if buffer is available
		return (obj.cp:receive() == 2);
	end
	
	obj.remainingData = function()
		obj.cp:sendDirect(9); -- tell it's reading remaining data in buffer
		return obj.cp:receive();
	end
	
	obj.read = function()
		return obj.cp:receive();
	end
	
	return obj
end

return lib