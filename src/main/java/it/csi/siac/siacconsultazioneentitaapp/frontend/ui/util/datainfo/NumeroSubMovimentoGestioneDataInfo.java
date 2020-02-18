/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/03/2017
 *
 */
public class NumeroSubMovimentoGestioneDataInfo extends BaseDataInfo {

	/**
	 * Costruttore
	 * @param name il nome del campo
	 * @param numeroSubMovimentoGestioneKey il numero del subaccertamento
	 * @param testataSub se sia un accertamento o un subaccertamento
	 */
	public NumeroSubMovimentoGestioneDataInfo(String name, String numeroSubMovimentoGestioneKey, String testataSub) {
		super(name, "%%NUM_MG%%", numeroSubMovimentoGestioneKey, testataSub);
	}

	@Override
	protected String preProcessPattern(String pattern, Object[] argumentsObject) {
		String patternNew = pattern;
		// Il parametro numero 1 indica se sono ub accertamento o un sub
		if(argumentsObject.length > 1 && "T".equals(argumentsObject[1])) {
			patternNew = patternNew.replaceAll("%%NUM_MG%%", "");
		} else {
			patternNew = patternNew.replaceAll("%%NUM_MG%%", "{0}");
		}
		
		for (int i = 0; i < argumentsObject.length; i++) {
			Object o = argumentsObject[i];
			if(o==null){
				patternNew = patternNew.replaceAll("\\{"+i+"(.*?)\\}", getDefaultForNullValues());
			}
		}
		return patternNew;
	}
}
