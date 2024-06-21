# Tictactoe Back-end

_Technology ที่ใช้_

-   Java Spring Boot
-   Websocket

# วิธีการ Run

สามารถรันได้ด้วยปุ่ม run spring boot
หรือสามารถเข้าได้ผ่าน heroku url
https://tictactoe-javasb-ws-api-test-56fc563f6ba3.herokuapp.com

# วิธีออกแบบโปรแกรมและ algorithm ที่ใช้

-   เริ่มจากการสร้าง Model class ซึ่งเป็นข้อมูลหลักๆของระบบ
-   สร้าง Storage ที่จะเก็บฟังก์ชันต่างๆที่เกี่ยวกับการจัดการ Storage นั้นๆ
-   จากนั้นจะสร้าง Service ซึ่งจะเป็นคลาสที่ใช้สำหรับจัดการ Logic และข้อมูลต่างๆภายในระบบ
-   ต่อด้วย Controller ที่จะทำหน้าที่รับ HTTP Request method ที่มีการส่งคำขอร้องมาได้
-   ปิดท้ายด้วย Web Configuration ที่จะเปิดการใช้งาน Websocket และระบุ path ที่จะใช้งาน

_MODEL CLASSES_
เป็นคลาสตั้งต้นว่าในระบบควรจะมีข้อมูลอะไรอยู่บ้าง
ซึ่งจะมีคลาสดังนี้

User - จะมีการเก็บข้อมูลของผู้ใช้ ดังนี้

-   userId: เป็น Id ของผู้ใช้นั้นๆ ซึ่งจะไม่ซ้ำกัน
-   username: ชื่อของผู้ใช้ และไม่สามารถตั้งให้ซ้ำกันได้
-   password: รหัสของผู้ใช้งาน
-   isLogin: เป็นค่าที่บ่งบอกว่าผู้ใช้งานคนนี้ได้ login หรือยัง
-   gameHistories: จะดึงจากคลาสเก็บมาเก็บไว้ในรูปแบบ Array

Game - จะมีการเก็บข้อมูลของเกม ดังนี้

-   gameId: เป็น Id ของเกมนั้นๆ ซึ่งจะไม่ซ้ำกัน
-   playerX: ดึงจากคลาส Player ซึ่งจะเป็นผู้เล่น X
-   playerO: ดึงจากคลาส Player ซึ่งจะเป็นผู้เล่น O
-   turn: ค่าที่บ่งบอกว่าเป็น turn ของผู้เล่นไหน
-   gameBoard: ดึงจากคลาส GameBoard
-   status: ดึงมาจาก enum GameStatus
-   winner: ค่าที่บ่งบอกว่าใครเป็นผู้ชนะ
-   gameHistory: ดึงมาจากคลาส GameHistory

ซึ่งภายในคลาสนี้จะมีฟังก์ชันที่สำหรับคำนวณผู้แพ้ชนะในเกม โดยจะมี

-   checkDraw: จะเช็คว่าตารางนั้นเต็มหรือยัง หากว่ายังไม่มีผู้ชนะแล้วตารางเต็ม ก็จะเสมอและ return true
-   checkWinner: จะเช็คว่าผู้เล่นคนไหนเป็นคนชนะ โดยจะมีการตรวจสอบจาก checkRows, checkCols และ checkDiagonals ซึ่งจะ return boolean บอกกลับไปว่าผู้เล่นคนนั้นๆชนะหรือไม่
-   checkRows: จะเช็คว่าใน row นั้นๆมีการลงครบตามจำนวน 3 หรือ 4 ช่องไหม และจะ return boolean
-   checkCols: จะเช็คว่าใน col นั้นๆมีการลงครบตามจำนวน 3 หรือ 4 ช่องไหม และจะ return boolean
-   checkDiagonals: จะเช็คว่าแถวในแนวทะแยงทั้งจากซ้ายไปขวา และขวาไปซ้ายนั้นมีการลงครบตามจำนวน 3 หรือ 4 ช่องไหม และจะ return boolean

Player - จะมีการเก็บข้อมูลของผู้เล่นในเกมนั้นๆ ดังนี้

-   playerId: จะดึงมาจาก userId ของผู้เล่น X, O
-   playerName: จะดึงมาจาก username ของผู้เล่น X, O
-   markType: เป็นตัวบ่งบอกว่าเป็นผู้เล่น X หรือ O

GameBoard - จะมีการเก็บข้อมูลบอร์ดของเกมนั้นๆ ดังนี้

-   board: จะเก็บค่าเป็น array 2 มิติ ซึ่งจะเก็บค่าการลง X หรือ O ในบอร์ดตามตำแหน่ง
-   size: ขนาดตารางของบอร์ด

GameStatus - จะมีการเก็บค่า enum ที่เป็นสถานะของเกมนั้นๆ ดังนี้

-   NEW: เมื่อมีการสร้างเกม จะเซ็ตให้อยู่ในค่านี้
-   IN_PROGRESS: เมื่อมีการเชื่อมต่อเกม และเล่นเกม จะเซ็ตให้อยู่ในค่านี้
-   FINISHED: เมื่อเกมเล่นจบจนหาผู้แพ้ชนะได้ จะเซ็ตให้อยู่ในค่านี้
-   TERMINATED: เมื่อ Terminate เกม

TicTacToe - จะมีการเก็บค่า enum ที่เป็น X, O และ DRAW

GameHistory - จะมีการเก็บข้อมูลประวัติการเล่นเกมนั้นๆ ดังนี้

-   allMoves: ดึงมาจากคลาส Move และเก็บในรูปแบบ Array ซึ่งจะเก็บลำดับการเดินของเกมนั้นๆ
-   startTime: เวลาที่เริ่มเกม
-   endTime: เวลาที่จบเกม

Move - จะมีการเก็บค่าตำแหน่งและประเภท mark ดังนี้

-   markType: ประเภท mark คือ X หรือ O
-   position: ระบุตำแหน่งที่ลงไว้ในตาราง ซึ่งจะเป็น Array โดยเป็น [row, col]

GamePlay - จะมีการเก็บค่าการเล่นเกมในแต่ละ turn ดังนี้

-   gameId: ระบุ gameId เพื่อสามารถส่งข้อมูลไปได้ถูกเกม
-   markType: ระบุประเภท mark ว่า turn นั้นๆลง mark อะไร
-   coordinateX: ระบุตำแหน่ง row
-   coordinateY: ระบุตำแหน่ง col

_STORAGE_
เป็นคลาสไว้สำหรับจัดการกับข้อมูลต่างๆในระบบโดยจะมี UserStorage และ GameStorage

UserStorage - ไว้สำหรับจัดการกับข้อมูล User ในระบบ
โดยมีฟังก์ชัน ดังนี้

-   addUser: เพิ่มข้อมูลคลาส user เข้าสู่ storage
-   getAllUsers: ดึงข้อมูล user ทั้งหมดที่มีอยู่ใน storage
-   getUserById: ดึงข้อมูล user โดยระบุจาก userId
-   getUserByUsername: ดึงข้อมูล user โดยระบุจาก username
-   userExists: เช็คว่ามี user นี้อยู่ใน storage ไหม โดยเช็คจาก username

GameStorage - ไว้สำหรับจัดการกับข้อมูล Game ในระบบ
โดยมีฟังก์ชัน ดังนี้

-   addGame: เพิ่มข้อมูลคลาส game เข้าสู่ storage
-   getAllGames: ดึงข้อมูล game ทั้งหมดที่มีอยู่ใน storage
-   getGameById: ดึงข้อมูล game โดยระบุจาก userId
-   getAllGameIdKeys: ดึง gameId ทั้งหมดจาก storage
-   containsGameById: เช็คว่ามี gameId นี้อยู่ใน storage ไหม
-   deleteGameById: ลบ game นั้นออกไปจาก storage โดยเช็คจาก gameId

_SERVICE_
เป็นคลาสที่ไว้จัดการกับ logic ต่างๆในระบบ โดยจะสัมพันธ์กันกับ Controller ซึ่งจะมี UserService และ GameService

UserService - จัดการเกี่ยวกับ User ต่างๆ และ return ค่า user กลับไป ดังนี้

-   getAllUsers: ดึงข้อมูล user ทั้งหมดจาก GameStorage
-   getUser: ดึงข้อมูล user จาก storage โดยเช็คจาก userId
-   registerUser: ลงทะเบียน user โดยถ้าหากมี username นั้นๆแล้วจะไม่สามารถลงทะเบียนได้
-   loginUser: เข้าสู่ระบบ user โดยถ้าหากเข้าสู่ระบบสำเร็จจะ set สถานะ isLogin คนนั้นๆเป็น true แต่ถ้าหากใส่ username, password ผิด หรือ user นั้นๆมีการ login เข้าสู่ระบบแล้ว จะไม่สามารถเข้าสู่ระบบได้
-   logoutUser: ออกจากระบบ user และ set สถานะ isLogin คนนั้นๆเป็น false

GameService - จัดการเกี่ยวกับ Game ต่างๆ และ return ค่า game กลับไป ดังนี้

-   getAllGames: ดึงข้อมูล game ทั้งหมดจาก GameStorage
-   getGame: ดึงข้อมูล game จาก storage โดยเช็คจาก gameId
-   createGame: สร้าง game ขึ้นมาโดยจะนำค่า username และ size มาตั้งค่าให้กับ Player X และขนาดของบอร์ด และ set ค่าเกมต่างๆขึ้นมาเป็นค่าเริ่มต้น
-   connectToGame: เชื่อมต่อ game จาก gameId และนำ username มาตั้งค่าให้กับ Player O โดยถ้าหากว่าใส่ gameId ผิด หรือมีผู้เล่นที่อยู่ในเกมนั้นๆครบแล้ว จะไม่สามารถเชื่อมต่อเกมนั้นๆได้
-   connectToRandomGame: จะเช็คจาก GameStorage ว่าในขณะนั้นมีเกมไหนที่ยังมีผู้เล่นไม่ครบ และเลือกเข้าเกมนั้น โดยจะเลือกจากเกมแรกที่ยังมีผู้เล่นว่างอยู่ใน storage
-   gamePlay: เริ่มเล่นเกม โดยจะรับค่ามาเป็น GamePlay เพื่อนำไป set GameBoard, turn, GameHistory และจะเช็คว่าในขณะที่ส่งข้อมูลมามีผู้ชนะหรือยัง โดยจะดึงจากฟังก์ชันในคลาส Game ถ้าหากว่ามีผู้ชนะแล้ว จะเรียกใช้งานฟังก์ชัน endGame
-   surrender: ยอมแพ้ โดยจะรับค่ามาเป็น gameId และ playerSurrender และเรียกใช้งานฟังก์ชัน endGame
-   endGame: รับค่ามาเป็น TicTacToe และ Game เพื่อนำไป set winner, gameStatus และเพิ่มข้อมูลเกมนั้นๆเข้าสู่ gameHistories ของ user ที่ได้เล่นเกมนี้ ทั้ง user X และ O
-   terminateGame: ปิดเกมด้วยการกดออกขณะที่ยังไม่ได้เล่น ซึ่งจะเป็นการลบเกมๆนั้นออกจาก GameStorage

_CONTROLLER_
เป็นคลาสที่ไว้จัดการกับ HTTP request ต่างๆ โดยจะสัมพันธ์กันกับ Service ซึ่งจะมี UserController และ GameController

UserController - จะจัดการกับ path /user ทั้งหมด และเรียกใช้งานฟังก์ชันต่างๆจาก UserService

-   getAllUsers: เป็น Get method จาก path /all และเรียกใช้งาน getAllUsers ใน service
-   getUser: เป็น Get method จาก path /{userId} โดยจะรับค่ามาเป็น PathVariable และเรียกใช้งาน getUser ใน service
-   register: เป็น Post method จาก path /register โดยจะรับค่ามาเป็น User และเรียกใช้งาน registerUser ใน service
-   login: เป็น Put method จาก path /login โดยจะรับค่ามาเป็น User และเรียกใช้งาน loginUser ใน service
-   logout: เป็น Put method จาก path /logout โดยจะรับค่ามาเป็น User และเรียกใช้งาน logoutUser ใน service

GameController - จะจัดการกับ path /game ทั้งหมด และเรียกใช้งานฟังก์ชันต่างๆจาก GameService

-   getAllGames: เป็น Get method จาก path /all และเรียกใช้งาน getAllGames ใน service
-   getGame: เป็น Get method จาก path /{gameId} โดยจะรับค่ามาเป็น PathVariable และเรียกใช้งาน getGame ใน service
-   create: เป็น Post method จาก path /create โดยจะรับค่ามาเป็น usernameX, size และเรียกใช้งาน createGame ใน service
-   connectTo: เป็น Put method จาก path /connect โดยจะรับค่ามาเป็น usernameO, gameId และเรียกใช้งาน connectToGame ใน service
-   connectToRandom: เป็น Post method จาก path /connect-random โดยจะรับค่ามาเป็น usernameO และเรียกใช้งาน connectToRandomGame ใน service
-   gamePlay: เป็น Post method จาก path /play โดยจะรับค่ามาเป็น GamePlay และเรียกใช้งาน gamePlay ใน service
-   surrender: เป็น Post method จาก path /surrender โดยจะรับค่ามาเป็น gameId, playerSurrender และเรียกใช้งาน surrender ใน service
-   terminate: เป็น Post method จาก path /terminate โดยจะรับค่ามาเป็น gameId และเรียกใช้งาน terminateGame ใน service
-   sendGameProgress: เป็นฟังก์ชันสำหรับการส่ง message เป็นข้อมูลไปยัง frontend ผ่าน websocket ด้วยการระบุ path ให้ตรงกันกับ frontend โดยจะใช้ path /topic/game-progress/

_WEB CONFIGURATION_
WebConfig - ระบุ frontend url เพื่อป้องกันการบล็อกของ CORS
WebSocketConfiguration - กำหนด path ไว้สำหรับการคุยกันของ frontend และ backend โดยกำหนดเป็น /topic และกำหนด endpoint ให้ทำงานที่ path /game
