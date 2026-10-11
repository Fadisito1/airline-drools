function Enviar($age=50,$member="Basic",$lug=15,$pref="Window",$kids=$false,$points=0,$delay=0,$dur=3) {
  $seats = @(
    @{number="12A";emergencyExit=$true;occupied=$false},
    @{number="14C";emergencyExit=$false;occupied=$false}
  )
  $req = @{
    passenger=@{name="Test";age=$age;membershipStatus=$member;luggageWeight=$lug;seatPreference=$pref;travelingWithChildren=$kids;loyaltyPoints=$points}
    flight=@{flightNumber="AV000";delayMinutes=$delay;durationHours=$dur}
    seats=$seats
  }
  Invoke-RestMethod -Uri http://localhost:8080/airline/evaluate -Method Post -ContentType "application/json" -Body ($req | ConvertTo-Json -Depth 5)
}
function Verificar($nombre, $ok) { if ($ok) { "[OK]    $nombre" } else { "[FALLO] $nombre" } }

$r = Enviar -member Gold -delay 90;     Verificar "R1 Gold + retraso 90 => upgrade" ($r.passenger.upgradedToBusiness -eq $true)
$r = Enviar -member Gold -delay 30;     Verificar "R1 Gold + retraso 30 => sin upgrade" ($r.passenger.upgradedToBusiness -eq $false)
$r = Enviar -member Silver -delay 90;   Verificar "R1 Silver + retraso 90 => sin upgrade" ($r.passenger.upgradedToBusiness -eq $false)

$r = Enviar -age 70;                    Verificar "R2 edad 70 => prioritario" ($r.passenger.priorityCheckIn -eq $true)
$r = Enviar -age 65;                    Verificar "R2 edad 65 => no" ($r.passenger.priorityCheckIn -eq $false)

$r = Enviar -lug 8;                     Verificar "R3 equipaje 8 => descuento 10" ($r.passenger.discountPercent -eq 10)
$r = Enviar -lug 12;                    Verificar "R3 equipaje 12 => sin descuento" ($r.passenger.discountPercent -eq 0)

$r = Enviar -member Platinum -lug 25 -delay 90
Verificar "R4 Platinum + 25 kg => no elegible y sin upgrade" (($r.passenger.eligibleForUpgrade -eq $false) -and ($r.passenger.upgradedToBusiness -eq $false))

$r = Enviar -age 25 -pref Any
Verificar "R5 edad 25 + Any => asiento 12A ocupado" (($r.passenger.assignedSeat -eq "12A") -and ($r.seats[0].occupied -eq $true))
$r = Enviar -age 50 -pref Any;          Verificar "R5 edad 50 => sin asiento" ($null -eq $r.passenger.assignedSeat)
$r = Enviar -age 25 -pref Window;       Verificar "R5 preferencia Window => sin asiento" ($null -eq $r.passenger.assignedSeat)

$r = Enviar -delay 200;                 Verificar "R6 retraso 200 => compensacion 200" ($r.passenger.compensation -eq 200)
$r = Enviar -delay 120;                 Verificar "R6 retraso 120 => sin compensacion" ($r.passenger.compensation -eq 0)

$r = Enviar -member Silver -dur 6 -points 1000; Verificar "R7 Silver + 6h => 1500 puntos" ($r.passenger.loyaltyPoints -eq 1500)
$r = Enviar -member Basic -dur 6 -points 1000;  Verificar "R7 Basic + 6h => sin puntos extra" ($r.passenger.loyaltyPoints -eq 1000)

$r = Enviar -lug 18 -dur 1.5;           Verificar "R8 18 kg + 1.5h => no permitido" ($r.passenger.luggageAllowed -eq $false)
$r = Enviar -lug 18 -dur 3;             Verificar "R8 18 kg + 3h => permitido" ($r.passenger.luggageAllowed -eq $true)

$r = Enviar -member Platinum;           Verificar "R9 Platinum => VIP" ($r.passenger.vipLoungeAccess -eq $true)
$r = Enviar -member Gold;               Verificar "R9 Gold => sin VIP" ($r.passenger.vipLoungeAccess -eq $false)

$r = Enviar -age 35 -pref Any -kids $true
Verificar "R10 con ninos + Any => asiento preferencial, sin salida de emergencia" (($r.passenger.preferentialSeat -eq $true) -and ($null -eq $r.passenger.assignedSeat))
$r = Enviar -age 35 -pref Window -kids $true; Verificar "R10 con ninos + Window => sin asiento preferencial" ($r.passenger.preferentialSeat -eq $false)
