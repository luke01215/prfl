param (
     [string]$inputFilePath="C:\LCD\GIT\prfl\2021\"
    ,[string]$outputFileName
    ,[int]$regularSeasonEnd=14
    ,[int]$playoffSeasonEnd=17
    ,[int]$totalWeeksOfStats=18
    ,[decimal]$totalNumberOfTeams=16.00
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
    [System.Collections.Generic.List[Player]]$playerList
    [System.Collections.Generic.List[decimal]]$totalPointList
    [decimal]$numberDrafted
    [decimal]$numberStarted
    [decimal]$teamCount
    [System.Collections.Generic.List[decimal]]$STDRanking

    Position() {
        $this.Initialize()
    }

    Position([string]$name, [decimal]$numberDrafted, [decimal]$numberStarted, [decimal]$teamCount) {
        $this.Initialize()
        $this.name = $name
        $this.numberDrafted = $numberDrafted
        $this.numberStarted = $numberStarted
        $this.teamCount = $teamCount
    }

    [void] Initialize() {
        $this.playerList = New-Object -TypeName "System.Collections.Generic.List[Player]"
        $this.totalPointList = New-Object -TypeName "System.Collections.Generic.List[decimal]"
        $this.STDRanking = New-Object -TypeName "System.Collections.Generic.List[decimal]"
    }

    [void] buildTotalPointList() {
        foreach($player in $this.playerList) {
            foreach($i in $player.totalSeasonPointList) {
                $this.totalPointList.Add($i) | Out-Null
            }
        }
        $this.buildStats()
    }

    [void] buildStats() {
        $this.setPositionAverage($this.totalPointList)
        $this.setPositionVariance($this.totalPointList)
        $this.setPositionStandardDeviation($this.totalPointList)
        $this.setMeanAndSTDForAllPlayers($this.playerList)
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

    [decimal] getStandardDeviations([System.Collections.Generic.List[decimal]]$pointList, [decimal]$distance) {
        return [Statistics]::getStandardDeviationsFromMean($pointList, $distance)
    }

    [void] setMeanAndSTDForAllPlayers([System.Collections.Generic.List[Player]]$playerList) {
        foreach($player in $playerList) {
            $player.positionAverage = $this.average
            $player.positionSTD = $this.standardDeviation
            $player.calculateDistanceFromMeanAndSTDPercentage()
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
    [decimal]$totalNumberOfTeams

    League([System.Collections.Generic.List[Player]]$playerList, [decimal]$totalNumberOfTeams) {
        $this.Initialize($totalNumberOfTeams)
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
        $this.rbPosition.buildTotalPointList()
        $this.wrPosition.buildTotalPointList()
        $this.tePosition.buildTotalPointList()
        $this.pkPosition.buildTotalPointList()
        $this.offPosition.buildTotalPointList()
        $this.defPosition.buildTotalPointList()
        $this.stPosition.buildTotalPointList()
        $this.coachPosition.buildTotalPointList()
    }

    [void] Initialize([int]$numberOfTeams) {
        $this.qbPosition = [Position]::new("QB", 2.00, 1.00, $numberOfTeams)
        $this.rbPosition = [Position]::new("RB", 4.00, 2.00, $numberOfTeams)
        $this.wrPosition = [Position]::new("WR", 4.00, 2.00, $numberOfTeams)
        $this.tePosition = [Position]::new("TE", 2.00, 1.00, $numberOfTeams)
        $this.pkPosition = [Position]::new("PK", 2.00, 1.00, $numberOfTeams)
        $this.offPosition = [Position]::new("Off", 2.00, 1.00, $numberOfTeams)
        $this.defPosition = [Position]::new("Def", 2.00, 1.00, $numberOfTeams)
        $this.stPosition = [Position]::new("ST", 2.00, 1.00, $numberOfTeams)
        $this.coachPosition = [Position]::new("Coach", 2.00, 1.00, $numberOfTeams)
    }

    [void]BuildSTDRankingForPlayerCount([string]$positionName, [int]$numberOfRankingGroups) {
        switch($positionName) {
            "QB" {
                $count = $this.qbPosition.numberDrafted * $this.qbPosition.teamCount
                for($i = 0; $i -lt $count; $i++) {
                    $player = $this.qbPosition.playerList[$i]
                    $this.qbPosition.STDRanking.Add($player.stdDeviationPercentage)
                }
            }
            "RB" {
                $count = $this.rbPosition.numberDrafted * $this.rbPosition.teamCount
                for($i = 0; $i -lt $count; $i++) {
                    $player = $this.rbPosition.playerList[$i]
                    $this.rbPosition.STDRanking.Add($player.stdDeviationPercentage)
                }
            }
            "WR" {
                $count = $this.wrPosition.numberDrafted * $this.wrPosition.teamCount
                for($i = 0; $i -lt $count; $i++) {
                    $player = $this.wrPosition.playerList[$i]
                    $this.wrPosition.STDRanking.Add($player.stdDeviationPercentage)
                }
            }
            "TE" {
                $count = $this.tePosition.numberDrafted * $this.tePosition.teamCount
                for($i = 0; $i -lt $count; $i++) {
                    $player = $this.tePosition.playerList[$i]
                    $this.tePosition.STDRanking.Add($player.stdDeviationPercentage)
                }
            }
            "PK" {
                $count = $this.pkPosition.numberDrafted * $this.pkPosition.teamCount
                for($i = 0; $i -lt $count; $i++) {
                    $player = $this.pkPosition.playerList[$i]
                    $this.pkPosition.STDRanking.Add($player.stdDeviationPercentage)
                }
            }
            "Off" {
                $count = $this.offPosition.numberDrafted * $this.offPosition.teamCount
                for($i = 0; $i -lt $count; $i++) {
                    $player = $this.offPosition.playerList[$i]
                    $this.offPosition.STDRanking.Add($player.stdDeviationPercentage)
                }
            }
            "Def" {
                $count = $this.defPosition.numberDrafted * $this.defPosition.teamCount
                for($i = 0; $i -lt $count; $i++) {
                    $player = $this.defPosition.playerList[$i]
                    $this.defPosition.STDRanking.Add($player.stdDeviationPercentage)
                }
            }
            "ST" {
                $count = $this.stPosition.numberDrafted * $this.stPosition.teamCount
                for($i = 0; $i -lt $count; $i++) {
                    $player = $this.stPosition.playerList[$i]
                    $this.stPosition.STDRanking.Add($player.stdDeviationPercentage)
                }
            }
            "Coach" {
                $count = $this.coachPosition.numberDrafted * $this.coachPosition.teamCount
                for($i = 0; $i -lt $count; $i++) {
                    $player = $this.coachPosition.playerList[$i]
                    $this.coachPosition.STDRanking.Add($player.stdDeviationPercentage)
                }
            }
            else {
                Write-Error "Position[$positionName] not defined in League"
            }
        }
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
    [decimal]$positionAverage
    [decimal]$positionSTD
    [decimal]$averageDistanceFromMean
    [decimal]$stdDeviationPercentage
    
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

    [void] calculateDistanceFromMeanAndSTDPercentage() {
        $this.averageDistanceFromMean = $this.totalSeasonAvg - $this.positionAverage
        $this.stdDeviationPercentage = $this.averageDistanceFromMean / $this.positionSTD
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
    $league.BuildSTDRankingForPlayerCount("QB", 4)
    $league.BuildSTDRankingForPlayerCount("RB", 4)
    $league.BuildSTDRankingForPlayerCount("WR", 4)
    $league.BuildSTDRankingForPlayerCount("TE", 4)
    $league.BuildSTDRankingForPlayerCount("PK", 4)
    $league.BuildSTDRankingForPlayerCount("Off", 4)
    $league.BuildSTDRankingForPlayerCount("Def", 4)
    $league.BuildSTDRankingForPlayerCount("ST", 4)
    $league.BuildSTDRankingForPlayerCount("Coach", 4)
    Write-Output "Test"

}
catch {
    Write-Error $_.Exception.Message
}