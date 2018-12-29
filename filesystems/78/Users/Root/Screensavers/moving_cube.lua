local animX = 0
local animY = 0
local speed = 10

local ang = 85

return function(gpu)
	local vx = math.cos(ang)
	local vy = math.sin(ang)
	animX = animX + vx * speed
	animY = animY + vy * speed
	if animX > 1180 then
		animX = 1180
		ang = math.random(ang - 10, ang + 10)
	end
	if animX < 0 then
		animX = 0
		ang = math.random(ang - 10, ang + 10)
	end
		if animY > 620 then
		animY = 620
		ang = math.random(ang - 10, ang + 10)
	end
	if animY < 0 then
		animY = 0
		ang = math.random(ang - 10, ang + 10)
	end
	gpu.setColor(0x000000)
	gpu.fillRect(0, 0, 1280, 720)
	gpu.setColor(0xAFAFAF)
	gpu.fillRect(animX, animY, 100, 100)
	gpu.flushBuffer()
	computer.sleep(16)
end, "Moving Cube", 2