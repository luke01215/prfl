param (
     [string]$inputFilePath="G:\GIT\prfl\2021\"
    ,[string]$outputFileName
    ,[int]$regularSeasonEnd=14
    ,[int]$playoffSeasonEnd=17
    ,[int]$totalWeeksOfStats=18
)

class Statistics {

    static [decimal] getAverage([System.Collections.Generic.List[decimal]]$list) {
        if ($null -eq $list) {
            return -1.00Path
        }
        elseif ($list.Count -eq 0) {
            return 0.00
        }
        else {
            $count = $list.Count
            $sum = 0.00
            for($i = 0; $i -lt $count; $i++) {
                $sum = $sum + $list[$i]
            }
            return $sum / $count
        }
        return -1.00
    }

    static [decimal] getVariance([System.Collections.Generic.List[decimal]]$list) {
        $average = [Statistics]::getAverage($list)
        $count = $list.Count
        [System.Collections.Generic.List[decimal]]$deviationFromMeanList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        [System.Collections.Generic.List[decimal]]$deviationFromMeanSquaredList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        foreach ($number in $list) {
            $deviationFromMean = $number - $average
            $deviationFromMeanList.Add($deviationFromMean) | Out-Null
        }
        foreach ($number in $deviationFromMeanList) {
            $deviationFromMeanSquared = $number * $number
            $deviationFromMeanSquaredList.Add($deviationFromMeanSquared) | Out-Null
        }
        $sumOfSquares = 0.0
        foreach ($number in $deviationFromMeanSquaredList) {
            $sumOfSquares = $sumOfSquares + $number
        }
        # $deviationFromMeanList.Clear()
        # $deviationFromMeanSquaredList.Clear()
        return $sumOfSquares / $count
    }

    static [decimal] getStandardDeviation([System.Collections.Generic.List[decimal]]$list) {
        $variance = [Statistics]::getVariance($list)
        return [math]::Sqrt($variance)
    }

}

class League {
    [System.Collections.Generic.List[Player]]$qbList
    [System.Collections.Generic.List[Player]]$rbList
    [System.Collections.Generic.List[Player]]$wrList
    [System.Collections.Generic.List[Player]]$teList
    [System.Collections.Generic.List[Player]]$pkList
    [System.Collections.Generic.List[Player]]$offList
    [System.Collections.Generic.List[Player]]$defList
    [System.Collections.Generic.List[Player]]$stList
    [System.Collections.Generic.List[Player]]$coachList

    League([System.Collections.Generic.List[Player]]$playerList) {
        $this.Initialize()
        foreach($player in $playerList) {
            switch($Player.PlayerName) {
                "QB" {
                    $this.qbList.Add($player) | Out-Null
                }
                "RB" {
                    $this.rbList.Add($player) | Out-Null
                }
                "WR" {
                    $this.wrList.Add($player) | Out-Null
                }
                "TE" {
                    $this.teList.Add($player) | Out-Null
                }
                "PK" {
                    $this.pkList.Add($player) | Out-Null
                }
                "Off" {
                    $this.offList.Add($player) | Out-Null
                }
                "Def" {
                    $this.defList.Add($player) | Out-Null
                }
                "ST" {
                    $this.stList.Add($player) | Out-Null
                }
                "Coach" {
                    $this.coachList.Add($player) | Out-Null
                }
                else {
                    Write-Error "Player[$($player.PlayerName)] not defined in League"
                }
            }
        }
    }

    [void] Initialize() {
        $this.qbList = New-Object -TypeName "System.Collections.Generic.List[Player]"
        $this.rbList = New-Object -TypeName "System.Collections.Generic.List[Player]"
        $this.wrList = New-Object -TypeName "System.Collections.Generic.List[Player]"
        $this.teList = New-Object -TypeName "System.Collections.Generic.List[Player]"
        $this.pkList = New-Object -TypeName "System.Collections.Generic.List[Player]"
        $this.offList = New-Object -TypeName "System.Collections.Generic.List[Player]"
        $this.defList = New-Object -TypeName "System.Collections.Generic.List[Player]"
        $this.stList = New-Object -TypeName "System.Collections.Generic.List[Player]"
        $this.coachList = New-Object -TypeName "System.Collections.Generic.List[Player]"
    }
}

class Player {
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
    [System.Collections.Generic.List[decimal]]$totalSeasonPointList
    [int]$regularSeasonEnd
    [int]$playoffSeasonEnd
    [int]$totalWeeksOfStats
    [string]$playerName
    [string]$teamName
    [string]$firstName
    [string]$lastName
    [decimal]$regularSeasonAvg
    [decimal]$playoffSeasonAvg
    [decimal]$totalSeasonAvg
    [decimal]$regularSeasonVariance
    [decimal]$playoffSeasonVariance
    [decimal]$totalSeasonVariance
    [decimal]$regularSeasonStandardDeviation
    [decimal]$playoffSeasonStandardDeviation
    [decimal]$totalSeasonStandardDeviation
    
    Player([string]$rank, [string]$fullPlayerName, [decimal]$points, [decimal]$average, [string]$owner, [int]$byeweek, [int]$salary, [System.Collections.Generic.List[decimal]]$seasonPointList, [int]$regularSeasonEnd, [int]$playoffSeasonEnd, [int]$totalWeeksOfStats) {
        [System.Collections.Generic.List[decimal]]$this.seasonPointList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        [System.Collections.Generic.List[decimal]]$this.regularSeasonPointList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        [System.Collections.Generic.List[decimal]]$this.playoffSeasonPointList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        [System.Collections.Generic.List[decimal]]$this.totalSeasonPointList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
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
        $this.CalculateTotalSeasonPointList()
        $this.CalculateRegularSeasonPointList()
        $this.CalculatePlayoffSeasonPointList()
    }

    [void] CalculateTotalSeasonPointList() {
        [System.Collections.Generic.List[decimal]]$list = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        for($i = 0; $i -lt $this.playoffSeasonEnd; $i++) {
            $number = $this.seasonPointList[$i]
            $list.Add($number)
        }
        $this.totalSeasonPointList = $list
        $this.totalSeasonAvg = $this.getAvgOfList($list)
        $this.totalSeasonVariance = $this.getVarianceOfList($list)
        $this.totalSeasonStandardDeviation = $this.getStandardDeviationOfList($list)
    }

    [void] CalculateRegularSeasonPointList() {
        [System.Collections.Generic.List[decimal]]$list = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        for($i = 0; $i -lt $this.regularSeasonEnd; $i++) {
            $number = $this.seasonPointList[$i]
            $list.Add($number)
        }
        $this.regularSeasonPointList = $list
        $this.regularSeasonAvg = $this.getAvgOfList($list)
        $this.regularSeasonVariance = $this.getVarianceOfList($list)
        $this.regularSeasonStandardDeviation = $this.getStandardDeviationOfList($list)
    }

    [void] CalculatePlayoffSeasonPointList() {
        [System.Collections.Generic.List[decimal]]$list = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        for($i = $this.regularSeasonEnd; $i -lt $this.playoffSeasonEnd; $i++) {
            $number = $this.seasonPointList[$i]
            $list.Add($number)
        }
        $this.playoffSeasonPointList = $list
        $this.playoffSeasonAvg = $this.getAvgOfList($list)
        $this.playoffSeasonVariance = $this.getVarianceOfList($list)
        $this.playoffSeasonStandardDeviation = $this.getStandardDeviationOfList($list)
    }

    [void] parseFullName() {
        $tempString = ""
        $index = $this.fullPlayerName.LastIndexOf(" ")
        $this.playerName = $this.fullPlayerName.Substring($index + 1)
        $tempString = $this.fullPlayerName.Substring(0, $index)
        $index = $tempString.LastIndexOf(" ")
        $this.teamName = $tempString.Substring($index + 1)
        $tempString = $tempString.Substring(0, $index)
        $index = $tempString.LastIndexOf(" ")
        $this.firstName = $tempString.Substring($index + 1)
        $this.lastName = $tempString.Substring(0, $index - 1)
    }

    [decimal] getAvgOfList([System.Collections.Generic.List[decimal]]$list) {
        $result = [Statistics]::getAverage($list)
        return $result
    }

    [decimal] getVarianceOfList([System.Collections.Generic.List[decimal]]$list) {
        $result = [Statistics]::getVariance($list)
        return $result
    }
    
    [decimal] getStandardDeviationOfList([System.Collections.Generic.List[decimal]]$list) {
        $result = [Statistics]::getStandardDeviation($list)
        return $result
    }

}

try {
    [System.Collections.Generic.List[Player]]$playerList = New-Object -TypeName "System.Collections.Generic.List[Player]"
    $files = Get-ChildItem -Path $inputFilePath -Filter "*.csv"
    foreach ($inputFileName in $files) {
        $file = Import-Csv -LiteralPath $inputFileName
        foreach($player in $file) {
            [System.Collections.Generic.List[decimal]]$list = New-Object -TypeName "System.Collections.Generic.List[decimal]"
            for($i = 1; $i -le $totalWeeksOfStats; $i++) {
                $number = $player.$i
                $list.Add($number)
            }
            [Player]$playerObj = [Player]::new($player.Rank, $player.Player, $player.Pts, $player.Avg, $player.Status, $player.Bye, $player.Salary, $list, $regularSeasonEnd, $playoffSeasonEnd, $totalWeeksOfStats)
            $playerList.Add($playerObj)
        }
    }
    $league = [League]::new($playerList)
    Write-Output "Test"

}
catch {
    Write-Error $_.Exception.Message
}