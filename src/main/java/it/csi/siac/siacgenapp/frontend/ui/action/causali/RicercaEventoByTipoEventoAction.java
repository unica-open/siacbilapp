/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.causali;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.RicercaEventoByTipoEventoBaseAction;
import it.csi.siac.siacgenapp.frontend.ui.model.causali.RicercaEventoByTipoEventoModel;

/**
 * Classe di action per la ricerca dell'evento a partire dal tipo di evento.
 * 
 * @author Marchino Alessandro
 * @author Simona Paggio
 * @version 1.0.0 - 30/03/2015
 * @version 1.1.0 - 06/10/2015 - adattato per GEN/GSA
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaEventoByTipoEventoAction extends RicercaEventoByTipoEventoBaseAction<RicercaEventoByTipoEventoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4238986980038232318L;
	
}
