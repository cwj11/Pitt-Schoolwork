pima = readmatrix('pima.txt');
for i = 1:8
    min(pima(:,i));
    max(pima(:,i));
end
for i = 1:8
    mean(pima(:,i));
    std(pima(:,i));
end
ind = find(pima(:,9) == 0);
pima0 = pima(ind,:);
ind = find(pima(:,9) == 1);
pima1 = pima(ind,:);
for i = 1:8
    mean(pima0(:,i));
    std(pima0(:,i));
end
for i = 1:8
    mean(pima1(:,i));
    std(pima1(:,i));
end
for i = 1:8
    corrcoef(pima(:,i),pima(:,9));
end
corrcoef(pima);
%histogram_analysis(pima1(:,2))
scatter_plot(pima(:,2),pima(:,5));







function h = histogram_analysis(v)
    hist(v,20)
end

function s = scatter_plot(v1, v2)
    scatter(v1,v2)
end