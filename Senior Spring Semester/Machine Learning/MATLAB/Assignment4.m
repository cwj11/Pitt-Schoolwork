housing = readmatrix("housing.txt");
housing(1:5,:);
housing(:,1);
for i = 1:13
    corrcoef(housing(:,i), housing(:,14));
end

scatter_plot(housing(:,13), housing(:,14))
title("MEDV vs LSTAT")

corrcoef(housing)





















function s = scatter_plot(v1, v2)
    scatter(v1,v2)
end