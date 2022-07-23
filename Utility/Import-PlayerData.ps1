param (
     [string]$inputFileName="C:\LCD\GIT\prfl\2021\2021_def.csv"
    ,[string]$outputFileName
    ,[int]$regularSeasonEnd=14
    ,[int]$playoffSeasonEnd=17
    ,[int]$totalWeeksOfStats=18
)

class Position {
    [string]$rank
    [string]$fullPlayerName
    [decimal]$points
    [decimal]$average
    [string]$owner
    [int]$byeweek
    [int]$salary
    [System.Collections.Generic.List[decimal]]$seasonPointList
    [System.Collections.Generic.List[decimal]]$regularSeasonPointList
    [System.Collections.Generic.List[decimal]]$playoffSeasonPointList
    

}

try {
    $file = Import-Csv -LiteralPath $inputFileName
    Write-Output "Test"
}
catch {
    Write-Error $_.Exception.Message
}