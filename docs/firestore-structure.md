# Firestore Structure

## users

```
users
 └─ userId
 └─ username
 └─ password
 └─ name
 └─ role
 └─ region
 └─ anganwadiCenter
 └─ createdAt
```

## children

```
children
 └─ childId
 └─ childName
 └─ dob
 └─ gender
 └─ parentName
 └─ parentPhone
 └─ village
 └─ region
 └─ anganwadiCenter
 └─ registeredBy
 └─ createdAt
```

## growthRecords

```
growthRecords
 └─ recordId
 └─ childId
 └─ heightCm
 └─ weightKg
 └─ bmi
 └─ nutritionStatus
 └─ confidence
 └─ createdAt
```

## alerts

```
alerts
 └─ alertId
 └─ childId
 └─ nutritionStatus
 └─ message
 └─ severity
 └─ isResolved
 └─ createdAt
```
## Counters Collection

Used for generating unique Child IDs.

counters
 └─ childCounter
      └─ lastNumber

Example:

{
  "lastNumber": 0
}

Generated Child IDs:

GSTC001
GSTC002
GSTC003
...

## Field Descriptions

### region

Used to identify the administrative area of the child or user.

Example:

```
region = Tiruchirappalli
```

### bmi

Body Mass Index calculated using estimated height and weight.

Example:

```
bmi = 18.5
```

Formula:

```
BMI = Weight (kg) / (Height in meters × Height in meters)
```

### nutritionStatus

Possible values:

```
Underweight
Normal
Overweight
Obese
``` 