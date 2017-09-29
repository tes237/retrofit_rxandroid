public class some_activity
{
	void oncreate()
	{
        mWeb.getDList("10","0",
                new Consumer<ResponseTestData>()
                {
                    @Override
                    public void accept(ResponseTestData objDatas) throws Exception
                    {
                        // TODO: Handle response.
                        String serverResult = objDatas.getResult();
                        TestData serverData = objDatas.getData();

                        if(serverResult.equals(JSON_SUCCESS))
                        {
                            mDataList.clear();

                            if(serverData.getDats() == null)
                            {
                                return;
                            }

                            for(int x = 0; x < serverData.getDatas().size(); x++)
                            {
                                TestBody tmpData = serverData.getBodies().get(x);
                                String idStr = tmpData.getId();
                                String titleStr = tmpData.getTitle();
                                int verified = tmpData.getVerified();

                                long transactionIndex = tmpData.getModifyTransactionIndex();
                                long updateTimne = tmpData.getUpdateTime();
                                String tmpUserIdStr = mGeneralApplication.getIdString();

                                titleStr = URLDecoder.decode(titleStr, "utf-8");

                                mDataList.add(new BodyItem(idStr, titleStr, verified, itNeedToDownload, transactionIndex, updateTimne));
                            }

                            mInfoListAdapter.notifyDataSetChanged();
                        }
                        else if(serverResult.equals(JSON_FAIL))
                        {

                        }
                    }
                }
                ,new Consumer<Throwable>()
                {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception
                    {
                        // TODO: Handle error.
                    }
                }, disposables
        );

    }
}