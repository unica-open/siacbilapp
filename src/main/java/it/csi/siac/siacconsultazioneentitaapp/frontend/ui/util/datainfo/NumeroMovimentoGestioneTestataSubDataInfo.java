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
public class NumeroMovimentoGestioneTestataSubDataInfo extends PopoverDataInfo {

	/**
	 * Costruttore
	 * @param name il nome del campo
	 * @param dataPlacement il posizionamento del popover
	 * @param descrizioneMovimentoGestioneKey la descrizione dell'accertamento
	 * @param annoMovimentoGestioneKey l'anno dell'accertamento
	 * @param numeroMovimentoGestioneKey il numero dell'accertamento
	 * @param numeroSubMovimentoGestioneKey il numero del subaccertamento
	 * @param testataSub se sia un accertamento o un subaccertamento
	 */
	public NumeroMovimentoGestioneTestataSubDataInfo(String name, String dataPlacement,String descrizioneMovimentoGestioneKey, String annoMovimentoGestioneKey, String numeroMovimentoGestioneKey, String numeroSubMovimentoGestioneKey, String testataSub) {
		super(name, "{0}", dataPlacement, "Descrizione", "%%NUM_MG%%", descrizioneMovimentoGestioneKey, annoMovimentoGestioneKey, numeroMovimentoGestioneKey, numeroSubMovimentoGestioneKey, testataSub);
	}

	@Override
	protected String preProcessPattern(String pattern, Object[] argumentsObject) {
		String patternNew = pattern;
		// Il parametro numero 4 indica se sono ub accertamento o un sub
		if(argumentsObject.length > 4 && "T".equals(argumentsObject[4])) {
			patternNew = patternNew.replaceAll("%%NUM_MG%%", "{1,number,#}/{2,number,#}");
		} else {
			patternNew = patternNew.replaceAll("%%NUM_MG%%", "{1,number,#}/{2,number,#}-{3}");
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
