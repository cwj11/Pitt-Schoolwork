Y_a <- scan('menchild30bach.dat')
Y_b <- scan('menchild30nobach.dat')
length_a = length(Y_a)
length_b = length(Y_b)
mean_a = mean(Y_a)
mean_b = mean(Y_b)
sum_a = sum(Y_a)
sum_b = sum(Y_b)

a_theta = 2
b_theta = 1


I = 5000
theta = numeric(I)
gamma_ab = c(8,16,32,64,128)

theta_difference = sapply(gamma_ab, function(abgamma) {
  a_gamma = abgamma
  theta_vals = numeric(I)
  gamma_vals = numeric(I)
  
  theta = mean_a
  gamma = mean_a/mean_b
  
  for (i in 1:I) {
    
    theta = rgamma(1, a_theta + sum_a + sum_b, b_theta + length_a + length_b*gamma)
    gamma = rgamma(1, a_gamma + sum_b, a_gamma + length_b*theta)
    
    theta_vals[i] = theta
    gamma_vals[i] = gamma
  }
  
  theta_a_vals = theta_vals
  theta_b_vals = theta_vals * gamma_vals
  
  mean(theta_b_vals - theta_a_vals)
  
})

plot(gamma_ab, theta_difference, type = 'l')

