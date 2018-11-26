package com.inno72.check.service;

import com.inno72.check.mapper.Inno72CheckSignInMapper;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.vo.MachineSignInVo;
import com.inno72.check.vo.SignVo;
import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72Machine;

import java.util.List;

public interface CheckSignInService extends Service<Inno72CheckSignIn> {


    Result<String> add(Inno72CheckSignIn signIn);

    Result<List<SignVo>> findByMonth(SignVo signVo);

    Result<List<MachineSignInVo>> findMachineSignList(Inno72Machine machine);
}
