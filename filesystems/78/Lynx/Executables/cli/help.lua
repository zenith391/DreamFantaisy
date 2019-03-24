-- Return scheme: function, isDosCompatible, title, description
return function()
	print("Help:")
	print("- crash: Just crashes the system")
	print("- serial: Manages serial ports")
	print("- help: This help message")
	print("- power: Manager power this computer")
	print("- power poweroff: Poweroff this computer")
	print("- power upoweroff: Unsafely (without OS approval) poweroff this computer")
end, true, "Help", "Displays help message"