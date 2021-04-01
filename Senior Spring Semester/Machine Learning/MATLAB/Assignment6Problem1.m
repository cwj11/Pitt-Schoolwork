% Part a
pima_train = readmatrix("pima_train.txt");
pima_test = readmatrix("pima_test.txt");

x_train = pima_train(:,1:8);
x_test = pima_test(:,1:8);
y_train = pima_train(:,9);
y_test = pima_test(:,9);

[w,b] = svml(x_train, y_train, 1);

% Part c
svlm_test = apply_svlm(x_test, w,b);
svlm_train = apply_svlm(x_train, w, b);

pred_test = svlm_test>0;
pred_train = svlm_train>0;

pred_test = double(pred_test);
pred_train = double(pred_train);

test_conf = confusionmat(y_test, pred_test);
train_conf = confusionmat(y_train, pred_train);

err_test = (test_conf(1,1) + test_conf(2,2))/length(pred_test);
err_train = (train_conf(1,1) + train_conf(2,2))/length(pred_train);

sens_test = test_conf(2,2)/(test_conf(2,2)+test_conf(2,1));
spec_test = test_conf(1,1)/(test_conf(1,1)+test_conf(1,2));

sens_train = train_conf(2,2)/(train_conf(2,2)+train_conf(2,1));
spec_train = train_conf(1,1)/(train_conf(1,1)+train_conf(1,2));

%Problem 2
[test_mean, test_std] = compute_norm_parameters(pima_test(:,1:8));
test_input = normalize(pima_test(:,1:8), test_mean, test_std);
[train_mean, train_std] = compute_norm_parameters(pima_train(:,1:8));
train_input = normalize(pima_train(:,1:8), train_mean, train_std);

w = Log_regression(train_input, pima_train(:,9), 2000);

col_ones = ones(size(test_input, 1), 1);
test_input = horzcat(col_ones, test_input); 
pred = zeros(length(test_input(:,1)),1);
for i = 1:length(test_input(:,1))
    temp = exp(test_input(i,:)*w);
    pred(i) = temp/(temp+1);
end


[X,Y,T, AUC] = perfcurve(pima_test(:,9), svlm_test, 1);
AUC
plot(X,Y);