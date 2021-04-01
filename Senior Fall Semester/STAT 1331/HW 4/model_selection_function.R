model_selection <- function(max_lags,y,x){
  AIC <- matrix(NA, nrow = max_lags, ncol = 1)
  BIC <- matrix(NA, nrow = max_lags, ncol = 1)
  print(x)
  n_obs <- dim(x)[1]
  
  p <- dim(x)[2]
  lags_y <- matrix(NA, nrow = n_obs, ncol = max_lags)
  for (i in 1:max_lags) {
    lags_y[(i+1):n_obs,i] <- y[1:(n_obs-i),1] 
  }
  X_all <- cbind(x,lags_y)
  X_reg <- X_all[(max_lags+1):n_obs,]
  y_reg <- as.matrix(y[(max_lags+1):n_obs,1])
  for (i in 1:max_lags) {
    n_param <- p+i
    est_result <- ols(X_reg[,1:n_param], y_reg)
    sigma2_u = est_result$sigma2_u
    AIC[i,1] <- log(sigma2_u) + 2*n_param/n_obs
    BIC[i,1] <- log(sigma2_u) + log(n_obs)*n_param/n_obs
  }
  op_lag_AIC = which.min(AIC)
  op_lag_BIC = which.min(BIC)
  obj = list(AIC = AIC, BIC = BIC, op_lag_AIC = op_lag_AIC, op_lag_BIC = op_lag_BIC)
}
