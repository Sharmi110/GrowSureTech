# Firestore Structure

## users

```
users
 └─ userId
 └─ name
 └─ email
 └─ role (parent / worker / districtOfficer)
 └─ region
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
