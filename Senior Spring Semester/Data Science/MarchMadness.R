
library(ISLR)
library(ggplot2)
library(dplyr)
library(MASS)
library(class)
library(glmnet)
library(pls)
library(tidyverse)
library(caret)
library(leaps)


bball = read.csv("cbb.csv")
bball = data.frame(bball)
bball = bball[,-22]
#bball = bball[bball["YEAR"]!=2019,]
games = read.csv("Big_Dance_CSV.csv")
games = games[(games$Year > 2012),]
games = data.frame(games)

march = matrix(numeric(41*length(games[,1])),ncol=41,nrow=length(games[,1]))
march = data.frame(march)



colnames(march) <- c('G','W','ADJOE','ADJDE','BARTHAG','EFG_O','EFG_D','TOR','TORD','ORB','DRB','FTR','FTRD','X2P_O','X2P_D','X3P_O','X3P_D','ADJ_T','WAB', 'SEED',  'G_opp','W_opp','ADJOE_opp','ADJDE_opp','BARTHAG_opp','EFG_O_opp','EFG_D_opp','TOR_opp','TORD_opp','ORB_opp','DRB_opp','FTR_opp','FTRD_opp','X2P_O_opp','X2P_D_opp','X3P_O_opp','X3P_D_opp','ADJ_T_opp','WAB_opp', 'SEED_opp', "Win")

for(i in 1:length(games[,1])){
  
  
  tryCatch({
    march[i,] = c(bball[(bball["TEAM"]==games[i,"Team"] & bball["YEAR"]==games[i,"Year"]),3:22], bball[(bball["TEAM"]==games[i,"Team.1"] & bball["YEAR"]==games[i,"Year"]),3:22],games[i,"Score"] > games[i, "Score.1"])
  }, warning = function(w) {
    
  }, error = function(e) {
    
  }, finally={
    
  })
}

march = march[(march["G"]!=0),]
march["W"] = march["W"]/march["G"]
march["W_opp"] = march["W_opp"]/march["G_opp"]
march = march[,-21]
march = march[,-1]

idx = sample(1:400, 300)
march_train = march[idx,]
march_test = march[-idx,]

mylogit = glm(Win~., data=march, family="binomial")
summary(mylogit)
step.model = stepAIC(mylogit, direction = "both", trace = FALSE)
summary(step.model)

pred = predict(step.model, march_test)
pred = pred>0.5
pred = as.integer(pred)
mean(abs(pred - march_test[,"Win"]))

bball21 = read.csv("cbb21.csv")
bball21["W"] = bball21["W"]/bball21["G"]
#bball21 = bball21[,-22]

madness = read.csv("Madness.csv")



for(i in seq(1,125, by=2)){
  input = matrix(numeric(80), ncol=40, nrow=2)
  input = data.frame(input)
  colnames(input) <- c('G','W','ADJOE','ADJDE','BARTHAG','EFG_O','EFG_D','TOR','TORD','ORB','DRB','FTR','FTRD','X2P_O','X2P_D','X3P_O','X3P_D','ADJ_T','WAB', 'SEED', 'G_opp','W_opp','ADJOE_opp','ADJDE_opp','BARTHAG_opp','EFG_O_opp','EFG_D_opp','TOR_opp','TORD_opp','ORB_opp','DRB_opp','FTR_opp','FTRD_opp','X2P_O_opp','X2P_D_opp','X3P_O_opp','X3P_D_opp','ADJ_T_opp','WAB_opp', 'SEED_opp')
  input[1,] = c(bball21[bball21["TEAM"]==madness[i,1],3:22],bball21[bball21["TEAM"]==madness[i+1,1],3:22])
  for(a in 1:length(input)){
    input[,1] = as.numeric(input[,1])
  }
  
  guess = predict(step.model, input, type="response")
  if(0.5 < guess[1]){
    madness[length(madness[,1])+1,1] = madness[i,1]
  }else{
    madness[length(madness[,1])+1,1] = madness[i+1,1]
  }
}

input = matrix(numeric(80), ncol=40, nrow=2)
input = data.frame(input)
colnames(input) <- c('G','W','ADJOE','ADJDE','BARTHAG','EFG_O','EFG_D','TOR','TORD','ORB','DRB','FTR','FTRD','X2P_O','X2P_D','X3P_O','X3P_D','ADJ_T','WAB', 'SEED', 'G_opp','W_opp','ADJOE_opp','ADJDE_opp','BARTHAG_opp','EFG_O_opp','EFG_D_opp','TOR_opp','TORD_opp','ORB_opp','DRB_opp','FTR_opp','FTRD_opp','X2P_O_opp','X2P_D_opp','X3P_O_opp','X3P_D_opp','ADJ_T_opp','WAB_opp', 'SEED_opp')
input[1,] = c(bball21[bball21["TEAM"]=="Ohio St.",3:22],bball21[bball21["TEAM"]=="Oral Roberts",3:22])
for(a in 1:length(input)){
  input[,1] = as.numeric(input[,1])
}

guess = predict(step.model, input, type="response")
print(guess)

