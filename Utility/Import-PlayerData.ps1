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
    [int]$regularSeasonEnd
    [int]$playoffSeasonEnd
    [int]$totalWeeksOfStats
    [string]$positionName
    [string]$teamName
    [string]$firstName
    [string]$lastName
    
    Position([string]$rank, [string]$fullPlayerName, [decimal]$points, [decimal]$average, [string]$owner, [int]$byeweek, [int]$salary, [System.Collections.Generic.List[decimal]]$seasonPointList, [int]$regularSeasonEnd, [int]$playoffSeasonEnd, [int]$totalWeeksOfStats) {
        [System.Collections.Generic.List[decimal]]$this.seasonPointList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        [System.Collections.Generic.List[decimal]]$this.regularSeasonPointList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        [System.Collections.Generic.List[decimal]]$this.playoffSeasonPointList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        $this.rank = $rank
        $this.fullPlayerName = $fullPlayerName
        $this.points = $points
        $this.average = $average
        $this.owner = $owner
        $this.byeweek = $byeweek
        $this.salary = $salary
        $this.seasonPointList = $seasonPointList
        $this.regularSeasonEnd = $regularSeasonEnd
        $this.playoffSeasonEnd = $playoffSeasonEnd
        $this.totalWeeksOfStats = $totalWeeksOfStats
        $this.parseFullName()
        $this.CalculateRegularSeasonPointList()
        $this.CalculatePlayoffSeasonPointList()
    }

    [void] CalculateRegularSeasonPointList() {
        [System.Collections.Generic.List[decimal]]$list = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        for($i = 0; $i -lt $this.regularSeasonEnd; $i++) {
            $number = $this.seasonPointList[$i]
            $list.Add($number)
        }
        $this.regularSeasonPointList = $list
    }

    [void] CalculatePlayoffSeasonPointList() {
        [System.Collections.Generic.List[decimal]]$list = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        for($i = $this.regularSeasonEnd; $i -lt $this.playoffSeasonEnd; $i++) {
            $number = $this.seasonPointList[$i]
            $list.Add($number)
        }
        $this.playoffSeasonPointList = $list
    }

    [void] parseFullName() {
        $tempString = ""
        $index = $this.fullPlayerName.LastIndexOf(" ")
        $this.positionName = $this.fullPlayerName.Substring($index + 1)
        $tempString = $this.fullPlayerName.Substring(0, $index)
        $index = $tempString.LastIndexOf(" ")
        $this.teamName = $tempString.Substring($index + 1)
        $tempString = $tempString.Substring(0, $index)
        $index = $tempString.LastIndexOf(" ")
        $this.firstName = $tempString.Substring($index + 1)
        $this.lastName = $tempString.Substring(0, $index - 1)
    }

}

try {
    [System.Collections.Generic.List[Position]]$positionList = New-Object -TypeName "System.Collections.Generic.List[Position]"
    $file = Import-Csv -LiteralPath $inputFileName
    foreach($position in $file) {
        [System.Collections.Generic.List[decimal]]$list = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        for($i = 1; $i -le $totalWeeksOfStats; $i++) {
            $number = $position.$i
            $list.Add($number)
        }
        [Position]$positionObj = [Position]::new($position.Rank, $position.Player, $position.Pts, $position.Avg, $position.Status, $position.Bye, $position.Salary, $list, $regularSeasonEnd, $playoffSeasonEnd, $totalWeeksOfStats)
        $positionList.Add($positionObj)
    }
    Write-Output "Test"

}
catch {
    Write-Error $_.Exception.Message
}