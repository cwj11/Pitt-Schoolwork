gaussian = readmatrix('gaussian.txt');
scatter(gaussian(:,1),gaussian(:,2));
mu = [mean(gaussian(:,1)), mean(gaussian(:,2))]
sigma = cov(gaussian);
x1 = 0:0.2:20;
x2 = 0:0.2:20;
[X1,X2] = meshgrid(x1,x2);
X = [X1(:) X2(:)];
y = mvnpdf(X,mu,sigma);
y = reshape(y,length(x2),length(x1));
surf(x1,x2,y)
caxis([min(y(:))-0.5*range(y(:)),max(y(:))])
axis([0 15 0 15 0 max(y(:))])
xlabel('x1')
ylabel('x2')
zlabel('Probability Density')


x1 = -2:0.2:10;
stdev = [std(gaussian(:,1)), std(gaussian(:,2))]
y1 = normpdf(x1, mu(1), stdev(1));
y2 = normpdf(x2, mu(2), stdev(2));
plot(x2, y2)