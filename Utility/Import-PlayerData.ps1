param (
     [string]$inputFilePath="C:\LCD\GIT\prfl\2021\"
    ,[string]$outputFileName
    ,[int]$regularSeasonEnd=14
    ,[int]$playoffSeasonEnd=17
    ,[int]$totalWeeksOfStats=18
    ,[int]$totalNumberOfTeams=16
)

class Statistics {

    static [decimal] getAverage([System.Collections.Generic.List[decimal]]$list) {
        if ($null -eq $list) {
            return -1.00
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
        return $sumOfSquares / $count
    }

    static [decimal] getStandardDeviation([System.Collections.Generic.List[decimal]]$list) {
        $variance = [Statistics]::getVariance($list)
        return [math]::Sqrt($variance)
    }

    static [decimal] getStandardDeviationsFromMean([System.Collections.Generic.List[decimal]]$list, [decimal]$numberOfDeviations) {
        $standardDeviation = [Statistics]::getStandardDeviation($list)
        $mean = [Statistics]::getAverage($list)
        $numOfStandardDeviations = $standardDeviation * $numberOfDeviations
        return $mean + $numOfStandardDeviations
    }

}

class Position {
    [string]$name
    [decimal]$average
    [decimal]$variance
    [decimal]$standardDeviation
    [decimal]$oneStandardDeviationFromMean
    [int]$playerCountThatFallWithinOneSTD
    [decimal]$twoStandardDeviationsFromMean
    [int]$playerCountThatFallWithinTwoSTD
    [decimal]$threeStandardDeviationsFromMean
    [int]$playerCountThatFallWithinThreeSTD

    [decimal]$restrictedOneStandardDeviationFromMean
    [int]$restrictedPlayerCountThatFallWithinOneSTD
    [decimal]$restrictedTwoStandardDeviationsFromMean
    [int]$restrictedPlayerCountThatFallWithinTwoSTD
    [decimal]$restrictedThreeStandardDeviationsFromMean
    [int]$restrictedPlayerCountThatFallWithinThreeSTD

    [System.Collections.Generic.List[Player]]$playerList
    [System.Collections.Generic.List[decimal]]$totalPointList
    [System.Collections.Generic.List[decimal]]$restrictedPointList
    [int]$numberDrafted
    [int]$numberStarted

    Position() {
        $this.Initialize()
    }

    Position([string]$name, [int]$numberDrafted, [int]$numberStarted) {
        $this.Initialize()
        $this.name = $name
        $this.numberDrafted = $numberDrafted
        $this.numberStarted = $numberStarted
    }

    [void] Initialize() {
        $this.playerList = New-Object -TypeName "System.Collections.Generic.List[Player]"
        $this.totalPointList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        $this.restrictedPointList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        $this.playerCountThatFallWithinOneSTD = 0
        $this.playerCountThatFallWithinTwoSTD = 0
        $this.playerCountThatFallWithinThreeSTD = 0
    }

    [void] buildTotalPointList() {
        foreach($player in $this.playerList) {
            foreach($i in $player.totalSeasonPointList) {
                $this.totalPointList.Add($i) | Out-Null
            }
        }
    }

    [void] buildRestrictedPointList([int]$numberOfPlayers) {
        for($i = 0; $i -lt $numberOfPlayers; $i++) {
            $player = $this.playerList[$i]
            foreach($j in $player.totalSeasonPointList) {
                $this.restrictedPointList.Add($j) | Out-Null
            }
        }
    }

    [void] buildStats() {
        $this.setPositionAverage($this.totalPointList)
        $this.setPositionVariance($this.totalPointList)
        $this.setPositionStandardDeviation($this.totalPointList)
        $this.setStandardDeviations($this.totalPointList)
        $this.setCountThatFallwithinDeviations()
    }

    [void] setPositionAverage([System.Collections.Generic.List[decimal]]$pointList) {
        $this.average = [Statistics]::getAverage($pointList)
    }

    [void] setPositionVariance([System.Collections.Generic.List[decimal]]$pointList) {
        $this.variance = [Statistics]::getVariance($pointList)
    }

    [void] setPositionStandardDeviation([System.Collections.Generic.List[decimal]]$pointList) {
        $this.standardDeviation = [Statistics]::getStandardDeviation($pointList)
    }

    [void] setStandardDeviations([System.Collections.Generic.List[decimal]]$pointList) {
        $this.oneStandardDeviationFromMean = [Statistics]::getStandardDeviationsFromMean($pointList, 1.00)
        $this.twoStandardDeviationsFromMean = [Statistics]::getStandardDeviationsFromMean($pointList, 2.00)
        $this.threeStandardDeviationsFromMean = [Statistics]::getStandardDeviationsFromMean($pointList, 3.00)
    }

    [decimal] getStandardDeviations([System.Collections.Generic.List[decimal]]$pointList, [decimal]$distance) {
        return [Statistics]::getStandardDeviationsFromMean($pointList, $distance)
    }

    [void] setCountThatFallwithinDeviations() {
        foreach($player in $this.playerList) {
            if($player.totalSeasonAvg -gt $this.oneStandardDeviationFromMean) {
                $this.playerCountThatFallWithinOneSTD++ 
            }
            if($player.totalSeasonAvg -gt $this.twoStandardDeviationsFromMean) {
                $this.playerCountThatFallWithinTwoSTD++ 
            }
            if($player.totalSeasonAvg -gt $this.threeStandardDeviationsFromMean) {
                $this.playerCountThatFallWithinThreeSTD++ 
            }
        }
    }
}

class League {
    [Position]$qbPosition
    [Position]$rbPosition
    [Position]$wrPosition
    [Position]$tePosition
    [Position]$pkPosition
    [Position]$offPosition
    [Position]$defPosition
    [Position]$stPosition
    [Position]$coachPosition
    [int]$totalNumberOfTeams

    League([System.Collections.Generic.List[Player]]$playerList, [int]$totalNumberOfTeams) {
        $this.Initialize()
        $this.totalNumberOfTeams = $totalNumberOfTeams
        foreach($player in $playerList) {
            switch($Player.positionName) {
                "QB" {
                    $this.qbPosition.playerList.Add($player) | Out-Null
                }
                "RB" {
                    $this.rbPosition.playerList.Add($player) | Out-Null
                }
                "WR" {
                    $this.wrPosition.playerList.Add($player) | Out-Null
                }
                "TE" {
                    $this.tePosition.playerList.Add($player) | Out-Null
                }
                "PK" {
                    $this.pkPosition.playerList.Add($player) | Out-Null
                }
                "Off" {
                    $this.offPosition.playerList.Add($player) | Out-Null
                }
                "Def" {
                    $this.defPosition.playerList.Add($player) | Out-Null
                }
                "ST" {
                    $this.stPosition.playerList.Add($player) | Out-Null
                }
                "Coach" {
                    $this.coachPosition.playerList.Add($player) | Out-Null
                }
                else {
                    Write-Error "Position[$($player.positionName)] not defined in League"
                }
            }
        }
        $this.qbPosition.buildTotalPointList()
        $this.qbPosition.buildRestrictedPointList($this.totalNumberOfTeams * $this.qbPosition.numberDrafted)
        $this.qbPosition.buildStats()
        $this.rbPosition.buildTotalPointList()
        $this.rbPosition.buildStats()
        $this.wrPosition.buildTotalPointList()
        $this.wrPosition.buildStats()
        $this.tePosition.buildTotalPointList()
        $this.tePosition.buildStats()
        $this.pkPosition.buildTotalPointList()
        $this.pkPosition.buildStats()
        $this.offPosition.buildTotalPointList()
        $this.offPosition.buildStats()
        $this.defPosition.buildTotalPointList()
        $this.defPosition.buildStats()
        $this.stPosition.buildTotalPointList()
        $this.stPosition.buildStats()
        $this.coachPosition.buildTotalPointList()
        $this.coachPosition.buildStats()
    }

    [void] Initialize() {
        $this.qbPosition = [Position]::new("QB", 2, 1)
        $this.rbPosition = [Position]::new("RB", 4, 2)
        $this.wrPosition = [Position]::new("WR", 4, 2)
        $this.tePosition = [Position]::new("TE", 2, 1)
        $this.pkPosition = [Position]::new("PK", 2, 1)
        $this.offPosition = [Position]::new("Off", 2, 1)
        $this.defPosition = [Position]::new("Def", 2, 1)
        $this.stPosition = [Position]::new("ST", 2, 1)
        $this.coachPosition = [Position]::new("Coach", 2, 1)
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
    [string]$positionName
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
        $this.positionName = $this.fullPlayerName.Substring($index + 1)
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
    $league = [League]::new($playerList, $totalNumberOfTeams)
    Write-Output "Test"

}
catch {
    Write-Error $_.Exception.Message
}