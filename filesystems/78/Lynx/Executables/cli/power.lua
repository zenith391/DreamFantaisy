-- Return scheme: function, isDosCompatible, title, description
return function(argc, argv)
	if argc < 2 then
		print("Usage: power <poweroff|upoweroff>")
		return
	end
	if argv[2] == "poweroff" then
		computer.shutdown()
		return
	end
	if argv[2] == "upoweroff" then
		computer.shutdownRaw()
		return
	end
end, true, "power", ""