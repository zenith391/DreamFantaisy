-- Return scheme: function, isDosCompatible, title, description
return function(argc, argv)
	if argc < 2 then
		print("Usage: serial <open|close>")
		return
	end
	if argc > 1 then
		if argv[2] == "open" then
			if argc < 3 then
				print("Usage: serial open [PORT]")
				return
			end
			return
		end
		if argv[2] == "close" then
			if argc < 3 then
				print("Usage: serial close [PORT]")
				return
			end
			return
		end
		print("Unknown parameter: " .. argv[2])
	end
end, true, "Serial", ""