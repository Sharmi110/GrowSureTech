# Firestore Structure

users
 └─ userId
 └─ name
 └─ email
 └─ role

children
 └─ childId
 └─ childName
 └─ dob
 └─ gender
 └─ parentName
 └─ parentPhone
 └─ village
 └─ anganwadiCenter

growthRecords
 └─ recordId
 └─ childId
 └─ height
 └─ weight
 └─ confidenceScore
 └─ status
 └─ date

alerts
 └─ alertId
 └─ childId
 └─ message
 └─ severity
 └─ date